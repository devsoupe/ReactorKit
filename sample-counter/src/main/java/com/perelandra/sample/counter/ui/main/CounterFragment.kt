package com.perelandra.sample.counter.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buxikorea.buxi.library.reactorkit.BaseReactorFragment
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.perelandra.reactorviewmodel.extension.bind
import com.perelandra.reactorviewmodel.extension.disposed
import com.perelandra.reactorviewmodel.extension.of
import com.perelandra.sample.counter.R
import kotlinx.android.synthetic.main.fragment_counter.*

class CounterFragment : BaseReactorFragment<CounterViewModel>() {

  companion object {
    fun newInstance() = CounterFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_counter, container, false)
  }

  override fun createViewModel(): CounterViewModel {
    return CounterViewModel().of(this)
  }

  override fun bindActions(viewModel: CounterViewModel) {
    RxView.clicks(plusButton)
      .map { CounterViewModel.Action.increase }
      .bind(to = viewModel.action)
      .disposed(by = disposeBag)

    RxView.clicks(minusButton)
      .map { CounterViewModel.Action.decrease }
      .bind(to = viewModel.action)
      .disposed(by = disposeBag)
  }

  override fun bindStates(viewModel: CounterViewModel) {
    viewModel.state.map { it.value }
      .distinctUntilChanged()
      .map { "$it" }
      .bind(to = RxTextView.text(valueTextView))
      .disposed(by = disposeBag)

    viewModel.state.map { it.isLoading }
      .distinctUntilChanged()
      .bind(to = RxView.visibility(progressBar, View.GONE))
      .disposed(by = disposeBag)
  }
}
