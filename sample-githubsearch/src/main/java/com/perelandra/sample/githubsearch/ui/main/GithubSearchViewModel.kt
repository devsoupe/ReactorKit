package com.perelandra.sample.githubsearch.ui.main

import android.os.Parcelable
import com.perelandra.reactorviewmodel.ReactorViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.parcel.Parcelize

class GithubSearchViewModel(initialState: State = State(query = "", repos = emptyList(), nextPage = 0, isLoadingNextPage = false)) :
  ReactorViewModel<GithubSearchViewModel.Action, GithubSearchViewModel.Mutation, GithubSearchViewModel.State>(initialState) {

  sealed class Action {
    data class updateQuery(val query: String) : Action()
    object loadNextPage
  }

  sealed class Mutation {
    data class setQuery(val query: String) : Mutation()
    data class setRepos(val repos: List<String>, val nextPage: Int)
    data class appendRepos(val repos: List<String>, val nextPage: Int)
    data class setLoadingNextPage(val isLoadingNextPage: Boolean)
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
    is Action.updateQuery -> {
      Observable.just(Mutation.setQuery(action.query))
    }
  }

  override fun reduce(state: State, mutation: Mutation): State  = when (mutation) {
    is Mutation.setQuery -> {
      state.copy(query = mutation.query)
    }

    else -> state
  }
}