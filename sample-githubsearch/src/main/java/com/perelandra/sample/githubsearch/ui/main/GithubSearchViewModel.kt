package com.perelandra.sample.githubsearch.ui.main

import android.os.Parcelable
import com.buxikorea.buxi.library.reactorkit.ReactorViewModel
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

class GithubSearchViewModel(initialState: State = State(query = "", repos = emptyArray(), nextPage = 0, isLoadingNextPage = false)) :
  ReactorViewModel<GithubSearchViewModel.Action, GithubSearchViewModel.Mutation, GithubSearchViewModel.State>(initialState) {

  sealed class Action {
    data class updateQuery(val query: String) : Action()
    object loadNextPage
  }

  sealed class Mutation {
    data class setQuery(val query: String) : Mutation()
    data class setRepos(val repos: Array<String>, val nextPage: Int)
    data class appendRepos(val repos: Array<String>, val nextPage: Int)
    data class setLoadingNextPage(val isLoadingNextPage: Boolean)
  }

  @Parcelize
  data class State(
    val name: String = GithubSearchViewModel::class.java.name,
    val query: String,
    val repos: Array<String>,
    val nextPage: Int,
    val isLoadingNextPage: Boolean
  ) : Parcelable

  override fun mutate(action: Action): Observable<Mutation> {
    return super.mutate(action)
  }

  override fun reduce(state: State, mutation: Mutation): State {
    return super.reduce(state, mutation)
  }
}