package com.perelandrax.reactorkit.jetpack

import androidx.lifecycle.ViewModel
import com.perelandrax.reactorkit.Reactor

abstract class ReactorViewModel<Action, Mutation, State> : Reactor<Action, Mutation, State>, ViewModel() {

  override fun onCleared() {
    super.onCleared()
    clear()
  }
}