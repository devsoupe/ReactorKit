package com.perelandrax.reactorkit

import com.perelandrax.reactorkit.extras.DisposeBag
import com.perelandrax.reactorkit.extras.disposed
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * A Reactor is an UI-independent layer which manages the state of a view. The foremost role of a
 * reactor is to separate control flow from a view. Every view has its corresponding reactor and
 * delegates all logic to its reactor. A reactor has no dependency to a view, so it can be easily
 * tested.
 *
 * Action - An action represents user actions.
 * Mutation - A mutation represents state changes.
 * State - A State represents the current state of a view.
 */
interface Reactor<Action, Mutation, State> : AssociatedObjectStore {

  private val _action: ActionSubject<Action>
    get() = if (stub.isEnabled) stub.action else associatedObject(actionKey)
      ?: associatedObject<ActionSubject<Action>>(actionKey, ActionSubject.create())

  private val _state: Observable<State>
    get() = if (stub.isEnabled) stub.state else associatedObject(stateKey)
      ?: associatedObject(stateKey, createStateStream())

  // The action from the view. Bind user inputs to this subject.
  val action: ActionSubject<Action>
    get() {
      // Creates a state stream automatically
      this._state

      return this._action
    }

  // The initial state.
  var initialState: State

  // The current state. This value is changed just after the state stream emits a new state.
  var currentState: State
    get() = associatedObject(currentStateKey, initialState)
    set(value) = this.setAssociatedObject(value, currentStateKey)

  // The state stream. Use this observable to observe the state changes.
  val state: Observable<State>
    get() = this._state

  // A dispose bag. It is disposed each time the `reactor` is assigned.
  private val disposeBag: DisposeBag
    get() = associatedObject(disposeBagKey, DisposeBag())

  private fun createStateStream(): Observable<State> {
    val action = this._action
    val transformedAction = transformAction(action)
    val mutation = transformedAction
      .flatMap { action ->
        try {
          mutate(action).onErrorResumeNext { t: Throwable -> throwException(t).run { Observable.empty() } }
        } catch (throwable: Throwable) {
          throwException(throwable).run { Observable.empty<Mutation>() }
        }
      }
    val transformedMutation = transformMutation(mutation)
    val state = transformedMutation
      .scan(initialState) { state, mutate ->
        try {
          reduce(state, mutate).apply { currentState = this }
        } catch (t: Throwable) {
          throwException(t).run { currentState }
        }
      }
      .onErrorResumeNext { t: Throwable -> throwException(t).run { Observable.empty() } }
      .startWith(initialState)
      .observeOn(AndroidSchedulers.mainThread())
    val transformedState = transformState(state)
      .doOnNext { currentState = it }
      .replay(1)
    transformedState.connect().disposed(disposeBag)
    return transformedState
  }

  //
  fun throwException(throwable: Throwable) = throwable.printStackTrace()
  //fun throwException(throwable: Throwable) = Handler(Looper.getMainLooper()).post { throw throwable }

  // Transforms the action. Use this function to combine with other observables. This method is
  // called once before the state stream is created.
  fun transformAction(action: Observable<Action>): Observable<Action> = action

  // Commits mutation from the action. This is the best place to perform side-effects such as
  // async tasks.
  fun mutate(action: Action): Observable<Mutation> = Observable.empty()

  // Transforms the mutation stream. Implement this method to transform or combine with other
  // observables. This method is called once before the state stream is created.
  fun transformMutation(mutation: Observable<Mutation>): Observable<Mutation> = mutation

  // Generates a new state with the previous state and the action. It should be purely functional
  // so it should not perform any side-effects here. This method is called every time when the
  // mutation is committed.
  fun reduce(state: State, mutation: Mutation): State = state

  // Transforms the state stream. Use this function to perform side-effects such as logging. This
  // method is called once after the state stream is created.
  fun transformState(state: Observable<State>): Observable<State> = state

  fun clear() {
    disposeBag.clear()
    clearAssociatedObject()
  }

  // Associated Object Keys
  companion object {
    private const val actionKey = "action"
    private const val currentStateKey = "currentState"
    private const val stateKey = "state"
    private const val disposeBagKey = "disposeBag"
    private const val stubKey = "stub"
  }

  // Stub
  val stub: Stub<Action, Mutation, State>
    get() = associatedObject(stubKey) ?: associatedObject(stubKey, Stub(this, disposeBag))
}


