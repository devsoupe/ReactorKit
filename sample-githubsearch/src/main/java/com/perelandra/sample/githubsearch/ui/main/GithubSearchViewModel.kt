package com.perelandra.sample.githubsearch.ui.main

import android.arch.lifecycle.Transformations.map
import android.net.http.HttpResponseCache
import android.os.Parcelable
import android.util.Log
import com.google.gson.*
import com.perelandra.reactorviewmodel.ReactorViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import com.google.gson.reflect.TypeToken



class GithubSearchViewModel() :
  ReactorViewModel<GithubSearchViewModel.Action, GithubSearchViewModel.Mutation, GithubSearchViewModel.State>() {

  companion object {
    private val TAG = GithubSearchViewModel::class.java.simpleName
  }

  override var initialState: State = State()

  private val okHttpClient = OkHttpClient()

  sealed class Action {
    data class UpdateQuery(val query: String) : Action()
    object LoadNextPage : Action()
  }

  sealed class Mutation {
    data class SetQuery(val query: String) : Mutation()
    data class SetRepos(val repos: List<String>, val nextPage: Int) : Mutation()
    data class AppendRepos(val repos: List<String>, val nextPage: Int) : Mutation()
    data class SetLoadingNextPage(val isLoadingNextPage: Boolean) : Mutation()
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
      is Action.UpdateQuery -> Observable.concat(
        // 1) set current state's query
        Observable.just(Mutation.SetQuery(action.query)),

        // 2) call API and set repos
        this.search(action.query, 1, action)
          // cancel previous request when the new `UpdateQuery` action is fired
          .takeUntil(this.action.filter(::isUpdateQueryAction))
          .map { Mutation.SetRepos(it.first, it.second) })

      else -> Observable.empty()
    }
  }

  override fun reduce(state: State, mutation: GithubSearchViewModel.Mutation): State = when (mutation) {
    is GithubSearchViewModel.Mutation.SetQuery -> state.copy(query = mutation.query)
    is GithubSearchViewModel.Mutation.SetRepos -> state.copy(repos = mutation.repos, nextPage = mutation.nextPage)
    else -> state
  }

  private fun url(query: String, page: Int): String =
    "https://api.github.com/search/repositories?q=$query&page=$page"

  private fun search(query: String, page: Int, action: Action): Observable<Pair<List<String>, Int>> {
    var emptyResult = Pair<List<String>, Int>(emptyList(), 0)
    val url = url(query, page)

    return requestGet(url)
      .subscribeOn(Schedulers.io())
      .map {
        val gson = GsonBuilder().create()
        val dict = gson.fromJson(it, JsonObject::class.java)
        val items = gson.fromJson(dict["items"], object : TypeToken<ArrayList<JsonElement>>() {}.type) as ArrayList<JsonElement>
        val repos = items.flatMap { arrayListOf(it.asJsonObject["full_name"].asString) }
        val nextPage = if (repos.isEmpty()) 0 else page + 1
        Pair(repos, nextPage)
      }
      .doOnError { Log.d(TAG, "${it.message}") }
      .onErrorReturn { emptyResult }
  }

  private fun isUpdateQueryAction(action: Action): Boolean {
    return (action is Action.UpdateQuery)
  }

  private fun requestGet(url: String): Observable<String> {
    return Observable.create {
      val request = Request.Builder().url(url).build()
      val response = okHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
          it.onError(e)
        }

        override fun onResponse(call: Call, response: Response) {
          if (response.isSuccessful) {
            response.body()?.string()?.let { res ->
              it.onNext(res)
              it.onComplete()
            }
            return
          }

          if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
            it.onError(Throwable("⚠️ GitHub API rate limit exceeded. Wait for 60 seconds and try again."))
            return
          }

          it.onError(Throwable("Unknown error."))
        }
      })
    }
  }
}