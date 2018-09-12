package com.perelandra.sample.githubsearch.ui.main

import android.arch.lifecycle.Transformations.map
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.util.Log
import com.perelandra.reactorviewmodel.ReactorViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import java.net.HttpURLConnection
import java.net.URL

class GithubSearchViewModel(initialState: State = State(query = "", repos = emptyList(), nextPage = 0, isLoadingNextPage = false)) :
  ReactorViewModel<GithubSearchViewModel.Action, GithubSearchViewModel.Mutation, GithubSearchViewModel.State>(initialState) {

  companion object {
    private val TAG = this::class.java.simpleName
  }

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
    val name: String = GithubSearchViewModel::class.java.name,
    val query: String,
    val repos: List<String>,
    val nextPage: Int,
    val isLoadingNextPage: Boolean
  ) : Parcelable

  override fun mutate(action: Action): Observable<Mutation> = when (action) {
    is Action.updateQuery -> Observable.concat(
      Observable.just(Mutation.setQuery(action.query)),
      search(query = action.query, page = 1).map { Mutation.setRepos(emptyList(), it) })

    else -> Observable.empty()
  }

  override fun reduce(state: State, mutation: GithubSearchViewModel.Mutation): State = when (mutation) {
    is Mutation.setQuery -> state.copy(query = mutation.query)
    is Mutation.setRepos -> state.copy(repos = mutation.repos, nextPage = mutation.nextPage)
    else -> state
  }

  private fun url(query: String, page: Int): URL =
    URL("https://api.github.com/search/repositories?q=$query&page=$page")

  private fun search(query: String, page: Int): Observable<Int> {
    val url = url(query = query, page = page)
    return Observable.just(url.openConnection())
      .subscribeOn(Schedulers.io())
      .doOnError { Log.i(TAG, "GithubSearchViewModel :: search : ${it.printStackTrace()}") }
      .flatMap { Observable.just((it as HttpURLConnection).responseCode) }

//      .subscribe {
//        Log.i(TAG, "GithubSearchViewModel :: search :: responseCode : $it")
//      }
//      .map { (it as HttpURLConnection).responseCode }
//      .doOnError {
//        if (it is HttpException)
//      }
  }
}