package com.buxikorea.buxi.library.reactorkit

import com.perelandra.reactorviewmodel.extension.DisposeBag

interface ReactorView<ReactorViewModel> {

  val disposeBag: DisposeBag
    get() = DisposeBag()

  var viewModel: ReactorViewModel?

  fun attachViewModel() = this.viewModel?.let {
    bindActions(it)
    bindStates(it)
  }

  fun detachViewModel() = this.disposeBag.clear()

  fun bindActions(viewModel: ReactorViewModel)

  fun bindStates(viewModel: ReactorViewModel)
}