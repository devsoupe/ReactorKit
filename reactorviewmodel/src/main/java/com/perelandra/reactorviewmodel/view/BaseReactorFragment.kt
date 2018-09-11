package com.perelandra.reactorviewmodel.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.perelandra.reactorviewmodel.ReactorView
import com.perelandra.reactorviewmodel.extension.DisposeBag

abstract class BaseReactorFragment<T> : Fragment(), ReactorView<T> {

  override val disposeBag: DisposeBag = DisposeBag()

  override val viewModel: T
    get() = createViewModel()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    attachViewModel()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    detachViewModel()
  }
}
