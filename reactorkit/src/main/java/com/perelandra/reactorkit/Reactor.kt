package com.perelandra.reactorkit

import com.perelandra.reactorkit.extras.DisposeBag
import com.perelandra.reactorkit.extras.disposed
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

interface Reactor<Action, Mutation, State> : AssociatedObjectStore {

  val action: ActionSubject<Action>
    //    get() = if (stub.isEnabled) stub.action else associatedObject<ActionSubject<Action>>(actionKey, ActionSubject.create())
    get() = associatedObject<ActionSubject<Action>>(actionKey, ActionSubject.create())

  var initialState: State

  var currentState: State
    get() = associatedObject(currentStateKey, initialState)
    set(value) = this.setAssociatedObject(value, currentStateKey)

  val state: Observable<State>
    //    get() = if (stub.isEnabled) stub.state else associatedObject(stateKey, createStateStream())
    get() = associatedObject(stateKey, createStateStream())

  private val disposeBag: DisposeBag
    get() = associatedObject(disposeBagKey, DisposeBag())

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
    transformedState.connect().disposed(disposeBag)
    return transformedState
  }

  fun transformAction(action: Observable<Action>): Observable<Action> = action

  fun mutate(action: Action): Observable<Mutation> = Observable.empty()

  fun transformMutation(mutation: Observable<Mutation>): Observable<Mutation> = mutation

  fun reduce(state: State, mutation: Mutation): State = state

  fun transformState(state: Observable<State>): Observable<State> = state

  fun clear() {
    disposeBag.clear()
    clearAssociatedObject()
  }

  companion object {
    private const val actionKey = "action"
    private const val currentStateKey = "currentState"
    private const val stateKey = "state"
    private const val disposeBagKey = "disposeBag"
    private const val stubKey = "stub"
  }

//  val stub: Stub<Action, Mutation, State>
//    get() = associatedObject(stubKey, Stub())
}
