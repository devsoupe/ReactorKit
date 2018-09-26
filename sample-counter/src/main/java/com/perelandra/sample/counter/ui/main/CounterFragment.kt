package com.perelandra.sample.counter.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.perelandra.reactorviewmodel.bind
import com.perelandra.reactorviewmodel.disposed
import com.perelandra.reactorviewmodel.of
import com.perelandra.reactorviewmodel.ReactorView
import com.perelandra.sample.counter.R
import kotlinx.android.synthetic.main.fragment_counter.*

class CounterFragment : Fragment(), ReactorView<CounterViewModel> {

  companion object {
    fun newInstance() = CounterFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.fragment_counter, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewmodel = CounterViewModel().of(this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    clearReactorView()
  }

  override fun bind(viewmodel: CounterViewModel): ReactorView<CounterViewModel> {
    // Actions
    RxView.clicks(plusButton)
      .map { CounterViewModel.Action.Increase }
      .bind(to = viewmodel.action)
      .disposed(by = disposeBag)

    RxView.clicks(minusButton)
      .map { CounterViewModel.Action.Decrease }
      .bind(to = viewmodel.action)
      .disposed(by = disposeBag)

    // States
    viewmodel.state.map { it.value }
      .distinctUntilChanged()
      .map { "$it" }
      .bind(to = RxTextView.text(valueTextView))
      .disposed(by = disposeBag)

    viewmodel.state.map { it.isLoading }
      .distinctUntilChanged()
      .bind(to = RxView.visibility(progressBar, View.GONE))
      .disposed(by = disposeBag)

    return this
  }
}
