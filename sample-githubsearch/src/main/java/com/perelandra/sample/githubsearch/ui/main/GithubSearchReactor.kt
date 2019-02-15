package com.perelandra.sample.githubsearch.ui.main

import android.os.Parcelable
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.perelandra.reactorkit.Reactor
import com.perelandra.sample.githubsearch.client.GithubSearchClientImpl
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize

class GithubSearchReactor :
    Reactor<GithubSearchReactor.Action, GithubSearchReactor.Mutation, GithubSearchReactor.State> {

  companion object {
    private val TAG = GithubSearchReactor::class.java.simpleName
  }

  override var initialState: State = State()

  private val client = GithubSearchClientImpl()
  private val gson = GsonBuilder().create()

  sealed class Action {
    data class updateQuery(val query: String) : Action()
    object loadNextPage : Action()
  }

  sealed class Mutation {
    data class setQuery(val query: String) : Mutation()
    data class setRepos(val repos: List<String>, val nextPage: Int) : Mutation()
    data class appendRepos(val repos: List<String>, val nextPage: Int) : Mutation()
    data class setLoadingNextPage(val isLoadingNextPage: Boolean) : Mutation()
  }

  @Parcelize
  data class State(
      val name: String = TAG,
      val query: String = "",
      val repos: List<String> = emptyList(),
      val nextPage: Int = 0,
      val isLoadingNextPage: Boolean = false
  ) : Parcelable

  override fun mutate(action: Action): Observable<Mutation> {
    return when (action) {
      is Action.updateQuery -> Observable.concat(
          // 1) set current state's query
          Observable.just(Mutation.setQuery(action.query)),

          // 2) call API and set repos
          this.search(action.query, 1, action)
              // cancel previous request when the new `updateQuery` action is fired
              .takeUntil(this.action.filter { isUpdateQueryAction(action) }.map { client.cancel() })
              .map { Mutation.setRepos(it.first, it.second) })

      else -> Observable.empty()
    }
  }

  override fun reduce(state: State, mutation: GithubSearchReactor.Mutation): State = when (mutation) {
    is GithubSearchReactor.Mutation.setQuery -> state.copy(query = mutation.query)
    is GithubSearchReactor.Mutation.setRepos -> state.copy(repos = mutation.repos, nextPage = mutation.nextPage)
    else -> state
  }

  private fun url(query: String, page: Int): String =
      "https://api.github.com/search/repositories?q=$query&page=$page"

  private fun search(query: String, page: Int, action: Action): Observable<Pair<List<String>, Int>> {
    var emptyResult = Pair<List<String>, Int>(emptyList(), 0)
    val url = url(query, page)

    return client.request(url)
        .subscribeOn(Schedulers.io())
        .map {
          val dict = gson.fromJson(it, JsonObject::class.java)
          val items = gson.fromJson(dict["items"], object : TypeToken<ArrayList<JsonElement>>() {}.type) as ArrayList<JsonElement>
          val repos = items.flatMap { arrayListOf(it.asJsonObject["full_name"].asString) }
          val nextPage = if (repos.isEmpty()) 0 else page + 1
          Pair(repos, nextPage)
        }
        .doOnError { Log.d(TAG, "${it.message}") }
        .onErrorReturn { emptyResult }
  }

  private fun isUpdateQueryAction(action: Action): Boolean = action is Action.updateQuery
}