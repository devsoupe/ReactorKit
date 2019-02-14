package com.perelandra.sample.counter.ui.main

import android.os.Parcelable
import com.perelandra.reactorkit.Reactor
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

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

  @Parcelize
  data class State(
      val name: String = TAG,
      val value: Int = 0,
      val isLoading: Boolean = false
  ) : Parcelable

  override fun mutate(action: Action): Observable<Mutation> = when (action) {
    is Action.Increase -> Observable.concat(
        Observable.just(Mutation.SetLoading(true)),
        Observable.just(Mutation.IncreaseValue), //.delay(500, TimeUnit.MILLISECONDS),
        Observable.just(Mutation.SetLoading(false)))

    is Action.Decrease -> Observable.concat(
        Observable.just(Mutation.SetLoading(true)),
        Observable.just(Mutation.DecreaseValue), //.delay(500, TimeUnit.MILLISECONDS),
        Observable.just(Mutation.SetLoading(false)))

    else -> Observable.empty()
  }

  override fun reduce(state: State, mutation: CounterReactor.Mutation): State = when (mutation) {
    is Mutation.IncreaseValue -> state.copy(value = state.value.inc())
    is Mutation.DecreaseValue -> state.copy(value = state.value.dec())
    is Mutation.SetLoading -> state.copy(isLoading = mutation.isLoading)
    else -> state
  }
}
