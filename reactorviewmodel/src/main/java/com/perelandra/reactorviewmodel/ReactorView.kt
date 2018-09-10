package com.buxikorea.buxi.library.reactorkit

import android.content.Context
import com.perelandra.reactorviewmodel.extension.DisposeBag

interface ReactorView<ReactorViewModel> {

  val disposeBag: DisposeBag
    get() = DisposeBag()

  private val viewModel: ReactorViewModel
    get() = createViewModel()

  fun createViewModel() : ReactorViewModel

  fun attachViewModel() {
    bindActions(viewModel)
    bindStates(viewModel)
  }

  fun detachViewModel() = this.disposeBag.clear()

  fun bindActions(viewModel: ReactorViewModel)

  fun bindStates(viewModel: ReactorViewModel)
}