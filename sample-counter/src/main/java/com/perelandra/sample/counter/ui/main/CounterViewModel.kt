package com.perelandra.sample.counter.ui.main

import android.os.Parcelable
import android.util.Log
import com.perelandra.reactorviewmodel.ReactorViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit

class CounterViewModel(initialState: State = State(value = 0, isLoading = false))
  : ReactorViewModel<CounterViewModel.Action, CounterViewModel.Mutation, CounterViewModel.State>(initialState) {

  companion object {
    private val TAG = CounterViewModel::class.java.simpleName
  }

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
    val value: Int,
    val isLoading: Boolean
  ) : Parcelable

  override fun mutate(action: Action): Observable<Mutation> = when (action) {
    is Action.Increase -> Observable.concat(
      Observable.just(Mutation.SetLoading(true)),
      Observable.just(Mutation.IncreaseValue).delay(500, TimeUnit.MILLISECONDS),
      Observable.just(Mutation.SetLoading(false)))

    is Action.Decrease -> Observable.concat(
      Observable.just(Mutation.SetLoading(true)),
      Observable.just(Mutation.DecreaseValue).delay(500, TimeUnit.MILLISECONDS),
      Observable.just(Mutation.SetLoading(false)))

    else -> Observable.empty()
  }

  override fun reduce(state: State, mutation: CounterViewModel.Mutation): State = when (mutation) {
    is Mutation.IncreaseValue -> state.copy(value = state.value.inc())
    is Mutation.DecreaseValue -> state.copy(value = state.value.dec())
    is Mutation.SetLoading -> state.copy(isLoading = mutation.isLoading)
    else -> state
  }
}
