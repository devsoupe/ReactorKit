package com.perelandra.sample.githubsearch.ui.main

import android.os.Parcelable
import android.util.Log
import com.perelandra.reactorviewmodel.ReactorViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import java.net.URL

class GithubSearchViewModel(initialState: State = State(query = "", repos = emptyList(), nextPage = 0, isLoadingNextPage = false)) :
  ReactorViewModel<GithubSearchViewModel.Action, GithubSearchViewModel.Mutation, GithubSearchViewModel.State>(initialState) {

  companion object {
    private val TAG = GithubSearchViewModel::class.java.simpleName
  }

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
    val query: String,
    val repos: List<String>,
    val nextPage: Int,
    val isLoadingNextPage: Boolean
  ) : Parcelable

  override fun mutate(action: Action): Observable<Mutation> = when (action) {
    is Action.UpdateQuery -> Observable.concat(
      // 1) set current state's query
      Observable.just(Mutation.SetQuery(action.query)),

      // 2) call API and set repos
      this.search(query = action.query, page = 1)
        .map { Mutation.SetRepos(repos = it.first, nextPage = it.second) })

    else -> Observable.empty()
  }

  override fun reduce(state: State, mutation: GithubSearchViewModel.Mutation): State = when (mutation) {
    is Mutation.SetQuery -> state.copy(query = mutation.query)
    is Mutation.SetRepos -> state.copy(repos = mutation.repos, nextPage = mutation.nextPage)
    else -> state
  }

  override fun transformMutation(mutation: Observable<Mutation>): Observable<Mutation> {
    return super.transformMutation(mutation).observeOn(Schedulers.io())
  }

  override fun transformState(state: Observable<State>): Observable<State> {
    return super.transformState(state).observeOn(AndroidSchedulers.mainThread())
  }

  private fun url(query: String, page: Int): URL =
    URL("https://api.github.com/search/repositories?q=$query&page=$page")

  private fun search(query: String, page: Int): Observable<Pair<List<String>, Int>> {
    var emptyResult = Pair<List<String>, Int>(emptyList(), 0)
    val url = url(query = query, page = page)

    return Observable.just(url.openConnection())
      .map { emptyResult }
      .doOnError { Log.i(TAG, "GithubSearchViewModel :: search : ${it.printStackTrace()}") }
      .onErrorReturn { emptyResult }

//      .flatMap { Observable.just((it as HttpURLConnection).responseCode) }
//      .subscribe {
//        Log.i(TAG, "GithubSearchViewModel :: search :: responseCode : $it")
//      }
//      .map { (it as HttpURLConnection).responseCode }
//      .doOnError {
//        if (it is HttpException)
//      }
  }
}