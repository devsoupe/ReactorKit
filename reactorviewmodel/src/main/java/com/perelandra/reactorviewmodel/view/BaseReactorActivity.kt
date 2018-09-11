package com.perelandra.reactorviewmodel.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import com.perelandra.reactorviewmodel.ReactorView
import com.perelandra.reactorviewmodel.extension.DisposeBag

abstract class BaseReactorActivity<T> : FragmentActivity(), ReactorView<T> {

  override val disposeBag: DisposeBag = DisposeBag()

  override val viewModel: T
    get() = createViewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    attachViewModel()
  }

  override fun onDestroy() {
    super.onDestroy()
    detachViewModel()
  }
}
