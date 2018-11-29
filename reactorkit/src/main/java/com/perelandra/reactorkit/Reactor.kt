package com.perelandra.reactorkit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

interface Reactor<Action, Mutation, State> : AssociatedObjectStore {

  companion object {
    private const val actionKey = "action"
    private const val initialStateKey = "initialState"
    private const val currentStateKey = "currentState"
    private const val stateKey = "state"
    private const val disposeBagKey = "disposeBag"
  }

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
    val action = this.action
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
      .observeOn(AndroidSchedulers.mainThread())
    val transformedState = transformState(state)
      .doOnNext { currentState = it }
      .replay(1)
    transformedState.connect().disposed(by = disposeBag)
    return transformedState
  }

  open fun transformAction(action: Observable<Action>): Observable<Action> = action

  open fun mutate(action: Action): Observable<Mutation> = Observable.empty()

  open fun transformMutation(mutation: Observable<Mutation>): Observable<Mutation> = mutation

  open fun reduce(state: State, mutation: Mutation): State = state

  open fun transformState(state: Observable<State>): Observable<State> = state

  fun clear() {
    disposeBag.clear()
    clearAssociatedObject(id)
  }
}

