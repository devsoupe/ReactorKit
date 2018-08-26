package com.perelandra.sample.githubsearch.ui.main

import android.os.Parcelable
import com.buxikorea.buxi.library.reactorkit.ReactorViewModel
import kotlinx.android.parcel.Parcelize

class GithubSearchViewModel(initialState: State = State()) :
  ReactorViewModel<GithubSearchViewModel.Action, GithubSearchViewModel.Mutation, GithubSearchViewModel.State>(initialState) {

  sealed class Action {

  }

  sealed class Mutation {

  }

  @Parcelize
  data class State(
    val name: String = GithubSearchViewModel::class.java.name
  ) : Parcelable
}