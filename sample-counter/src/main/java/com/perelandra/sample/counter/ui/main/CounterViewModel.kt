package com.perelandra.sample.counter.ui.main

import android.os.Parcelable
import com.perelandra.reactorviewmodel.ReactorViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit

class CounterViewModel(initialState: State = State(value = 0, isLoading = false))
  : ReactorViewModel<CounterViewModel.Action, CounterViewModel.Mutation, CounterViewModel.State>(initialState) {

  sealed class Action {
    object increase : Action()
    object decrease : Action()
  }

  sealed class Mutation {
    object increaseValue : Mutation()
    object decreaseValue : Mutation()
    data class setLoading(val isLoading: Boolean) : Mutation()
  }

  @Parcelize
  data class State(
    val name: String = CounterViewModel::class.java.name,
    val value: Int,
    val isLoading: Boolean
  ) : Parcelable

  override fun mutate(action: Action): Observable<Mutation> = when (action) {
    is Action.increase -> Observable.concat(
      Observable.just(Mutation.setLoading(true)),
      Observable.just(Mutation.increaseValue).delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()),
      Observable.just(Mutation.setLoading(false)))

    is Action.decrease -> Observable.concat(
      Observable.just(Mutation.setLoading(true)),
      Observable.just(Mutation.decreaseValue).delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()),
      Observable.just(Mutation.setLoading(false))
    )

    else -> Observable.empty()
  }

  override fun reduce(state: State, mutation: CounterViewModel.Mutation): State = when (mutation) {
    is CounterViewModel.Mutation.increaseValue -> state.copy(value = state.value.inc())
    is CounterViewModel.Mutation.decreaseValue -> state.copy(value = state.value.dec())
    is CounterViewModel.Mutation.setLoading -> state.copy(isLoading = mutation.isLoading)
    else -> state
  }
}
