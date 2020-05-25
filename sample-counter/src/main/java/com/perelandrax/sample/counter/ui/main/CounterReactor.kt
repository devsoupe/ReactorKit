package com.perelandrax.sample.counter.ui.main

import com.perelandrax.reactorkit.Reactor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class CounterReactor
  : Reactor<CounterReactor.Action, CounterReactor.Mutation, CounterReactor.State> {

  companion object {
    private val TAG = CounterReactor::class.java.simpleName
  }

  override var initialState: State = State()

  sealed class Action {
    object Increase : Action()
    object Decrease : Action()
  }

  sealed class Mutation {
    object IncreaseValue : Mutation()
    object DecreaseValue : Mutation()
    data class SetLoading(val isLoading: Boolean) : Mutation()
  }

  data class State(
    val count: Int = 0,
    val isLoading: Boolean = false
  )

  override fun mutate(action: Action): Observable<Mutation> = when (action) {
    is Action.Increase -> Observable.concat(
      Observable.just(Mutation.SetLoading(true)),
      Observable.just(Mutation.IncreaseValue).delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()),
      Observable.just(Mutation.SetLoading(false)))

    is Action.Decrease -> Observable.concat(
      Observable.just(Mutation.SetLoading(true)),
      Observable.just(Mutation.DecreaseValue).delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()),
      Observable.just(Mutation.SetLoading(false)))
  }

  override fun reduce(state: State, mutation: Mutation): State = when (mutation) {
    is Mutation.IncreaseValue -> state.copy(count = state.count.inc())
    is Mutation.DecreaseValue -> state.copy(count = state.count.dec())
    is Mutation.SetLoading -> state.copy(isLoading = mutation.isLoading)
  }
}
