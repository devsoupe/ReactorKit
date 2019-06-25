package com.perelandra.reactorkit

import com.perelandra.reactorkit.extras.DisposeBag
import com.perelandra.reactorkit.extras.disposed

class Stub<Action, Mutation, State>(reactor: Reactor<Action, Mutation, State>, disposeBag: DisposeBag) {

  var isEnabled: Boolean = false

  val state: StateRelay<State> = StateRelay.create()
  val action: ActionSubject<Action> = ActionSubject.create()
  val actions: MutableList<Action> = mutableListOf()

  init {
    state.accept(reactor.initialState)

    state.subscribe { state ->
      reactor.currentState = state
    }.disposed(disposeBag)

    action.subscribe { action ->
      actions.add(action)
    }.disposed(disposeBag)
  }
}
