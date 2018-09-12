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
  }

  fun detachViewModel() {
    disposeBag.clear()
  }

  fun bindActions(viewModel: VM)
  fun bindStates(viewModel: VM)
}