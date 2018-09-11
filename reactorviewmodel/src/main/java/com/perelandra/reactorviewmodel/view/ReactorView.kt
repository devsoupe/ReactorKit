package com.perelandra.reactorviewmodel.view

import android.util.Log
import com.perelandra.reactorviewmodel.BuildConfig
import com.perelandra.reactorviewmodel.extension.DisposeBag

interface ReactorView<VM> {

  companion object {
    private val TAG = this::class.java.simpleName
  }

  val disposeBag: DisposeBag

  val viewModel: VM

  fun onCreateViewModel(): VM

  fun attachViewModel() {
    bindActions(viewModel)
    bindStates(viewModel)

    if (BuildConfig.DEBUG) {
      Log.i(TAG, "attachViewModel :: disposeBag size : ${disposeBag.size()}")
    }
  }

  fun detachViewModel() {
    disposeBag.clear()

    if (BuildConfig.DEBUG) {
      Log.i(TAG, "detachViewModel :: disposeBag size :  ${disposeBag.size()}")
    }
  }

  fun bindActions(viewModel: VM)

  fun bindStates(viewModel: VM)
}