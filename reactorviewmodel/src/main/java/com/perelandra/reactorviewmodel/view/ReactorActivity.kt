package com.perelandra.reactorviewmodel.view

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.perelandra.reactorviewmodel.extension.DisposeBag

abstract class ReactorActivity<T> : FragmentActivity(), ReactorView<T> {

  override val disposeBag: DisposeBag = DisposeBag()

  override val viewModel: T
    get() = onCreateViewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    attachViewModel()
  }

  override fun onDestroy() {
    super.onDestroy()
    detachViewModel()
  }
}
