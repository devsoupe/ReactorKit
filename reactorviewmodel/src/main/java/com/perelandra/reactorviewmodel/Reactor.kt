package com.perelandra.reactorviewmodel

import io.reactivex.Observable

interface Reactor<Action, Mutation, State> : AssociatedObjectStore {

  val action: ActionSubject<Action>
    get() {
      getAssociatedObject<ActionSubject<Action>>(actionKey)?.run { return this }
      return getAssociatedObject<ActionSubject<Action>>(actionKey, ActionSubject.create())
    }

  var initialState: State

  private var currentState: State
    get() = getAssociatedObject<State>(currentStateKey, initialState)
    set(value) {
      this.setAssociatedObject(value, currentStateKey)
    }

  val state: Observable<State>
    get() {
      getAssociatedObject<Observable<State>>(stateKey)?.run { return this }
      return getAssociatedObject<Observable<State>>(stateKey, createStateStream())
    }

  private val disposeBag: DisposeBag
    get() {
      getAssociatedObject<DisposeBag>(disposeBagKey)?.run { return this }
      return getAssociatedObject<DisposeBag>(disposeBagKey, DisposeBag())
    }

  private fun createStateStream(): Observable<State> {
    val transformedAction = transformAction(action)
    val mutation = transformedAction
      .flatMap { action ->
        mutate(action).onErrorResumeNext { _: Throwable -> Observable.empty() }
      }
    val transformedMutation = transformMutation(mutation)
    val state = transformedMutation
      .scan(initialState) { state, mutate -> reduce(state, mutate) }
      .onErrorResumeNext { _: Throwable -> Observable.empty() }
      .startWith(initialState)
    val transformedState = transformState(state)
      .doOnNext { currentState = it }
      .replay(1)
    return transformedState.apply { connect().disposed(by = disposeBag) }
  }

  open fun transformAction(action: Observable<Action>): Observable<Action> = action

  open fun mutate(action: Action): Observable<Mutation> = Observable.empty()

  open fun transformMutation(mutation: Observable<Mutation>): Observable<Mutation> = mutation

  open fun reduce(state: State, mutation: Mutation): State = state

  open fun transformState(state: Observable<State>): Observable<State> = state

  fun clearReactor() {
    disposeBag.clear()
    clearAssociatedObject(id)
  }
}

private var actionKey = "action"
private var initialStateKey = "initialState"
private var currentStateKey = "currentState"
private var stateKey = "state"
private var disposeBagKey = "disposeBag"
