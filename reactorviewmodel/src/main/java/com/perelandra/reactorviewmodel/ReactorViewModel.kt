package com.perelandra.reactorviewmodel

import android.arch.lifecycle.ViewModel

abstract class ReactorViewModel<Action, Mutation, State>() : Reactor<Action, Mutation, State>, ViewModel() {

  override fun onCleared() {
    super.onCleared()
    clearReactor()
  }
}

