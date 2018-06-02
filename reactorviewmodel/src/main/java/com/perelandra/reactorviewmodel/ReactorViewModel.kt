package com.buxikorea.buxi.library.reactorkit

import android.arch.lifecycle.ViewModel
import com.perelandra.reactorviewmodel.extension.ActionSubject
import com.perelandra.reactorviewmodel.extension.DisposeBag
import com.perelandra.reactorviewmodel.extension.disposed
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class ReactorViewModel<Action, Mutation, State>(private val initialState: State) : ViewModel() {

  val action: ActionSubject<Action> = ActionSubject.create()

  var currentState: State = initialState

  final val state: Observable<State> by lazy { createStateStream() }

  private final val disposeBag: DisposeBag = DisposeBag()

  open fun mutate(action: Action): Observable<Mutation> = Observable.empty()

  open fun reduce(state: State, mutation: Mutation): State = state

  open fun transformAction(action: Observable<Action>): Observable<Action> = action

  open fun transformMutation(mutation: Observable<Mutation>): Observable<Mutation> = mutation

  open fun transformState(state: Observable<State>): Observable<State> = state

  private final fun createStateStream(): Observable<State> {
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

  override fun onCleared() {
    super.onCleared()
    disposeBag.clear()
  }
}
