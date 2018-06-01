package com.buxikorea.buxi.library.reactorkit

import com.perelandra.reactorviewmodel.extension.DisposeBag

interface ReactorView<ReactorViewModel> {

  val disposeBag: DisposeBag
    get() = DisposeBag()

  var viewModel: ReactorViewModel?

  fun attachViewModel() = this.viewModel?.let { bind(it) }

  fun detachViewModel() = this.disposeBag.clear()

  fun bind(viewModel: ReactorViewModel)
}