package com.perelandrax.reactorkit

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class Stub<Action, Mutation, State>(reactor: Reactor<Action, Mutation, State>, disposables: CompositeDisposable) {

  var isEnabled: Boolean = false

  val state: StateRelay<State> = StateRelay.create()
  val action: ActionSubject<Action> = ActionSubject.create()
  val actions: MutableList<Action> = mutableListOf()

  init {
    state.accept(reactor.initialState)

    state.subscribe { state ->
      reactor.currentState = state
    }.addTo(disposables)

    action.subscribe { action ->
      actions.add(action)
    }.addTo(disposables)
  }
}
