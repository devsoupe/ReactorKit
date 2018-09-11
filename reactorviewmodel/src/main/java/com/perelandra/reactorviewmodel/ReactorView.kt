package com.perelandra.reactorviewmodel

import android.util.Log
import com.perelandra.reactorviewmodel.BuildConfig
import com.perelandra.reactorviewmodel.extension.DisposeBag

interface ReactorView<T> {

  val disposeBag: DisposeBag

  val viewModel: T

  fun createViewModel(): T

  fun attachViewModel() {
    bindActions(viewModel)
    bindStates(viewModel)

    Log.i(this::class.java.simpleName, "[ReactorView] attachViewModel : ${disposeBag.size()}")
  }

  fun detachViewModel() {
    disposeBag.clear()

    if (BuildConfig.DEBUG) {
      Log.i(this::class.java.simpleName, "[ReactorView] detachViewModel : ${disposeBag.size()}")
    }
  }

  fun bindActions(viewModel: T)

  fun bindStates(viewModel: T)
}