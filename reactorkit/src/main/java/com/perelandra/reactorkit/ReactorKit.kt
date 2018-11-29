package com.perelandra.reactorkit

import android.arch.lifecycle.ViewModel

abstract class ReactorKit<Action, Mutation, State>() : Reactor<Action, Mutation, State>, ViewModel() {

  override fun onCleared() {
    super.onCleared()
    clearReactor()
  }
}

