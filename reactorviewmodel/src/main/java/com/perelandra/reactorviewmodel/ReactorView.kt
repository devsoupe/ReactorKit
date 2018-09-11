package com.buxikorea.buxi.library.reactorkit

import com.perelandra.reactorviewmodel.extension.DisposeBag

interface ReactorView<T> {

  val disposeBag: DisposeBag

  val viewModel: T

  fun createViewModel(): T

  fun attachViewModel() {
    bindActions(viewModel)
    bindStates(viewModel)
  }

  fun detachViewModel() = disposeBag.clear()

  fun bindActions(viewModel: T)

  fun bindStates(viewModel: T)
}