package com.buxikorea.buxi.library.reactorkit

import com.perelandra.reactorviewmodel.extension.DisposeBag

interface ReactorView<T> {

  val disposeBag: DisposeBag
    get() = DisposeBag()

  val viewModel: T
    get() = createViewModel()

  fun createViewModel() : T

  fun attachViewModel() {
    bindActions(viewModel)
    bindStates(viewModel)
  }

  fun detachViewModel() = this.disposeBag.clear()

  fun bindActions(viewModel: T)

  fun bindStates(viewModel: T)
}