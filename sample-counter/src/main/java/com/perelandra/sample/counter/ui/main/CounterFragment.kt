package com.perelandra.sample.counter.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.perelandra.reactorkit.bind
import com.perelandra.reactorkit.disposed
import com.perelandra.reactorkit.ReactorView
import com.perelandra.sample.counter.R
import kotlinx.android.synthetic.main.fragment_counter.*

class CounterFragment : Fragment(), ReactorView<CounterReactor> {

  companion object {
    fun newInstance() = CounterFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.fragment_counter, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    reactor = CounterReactor()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    reactor.clear();
    clearReactorView()
  }

  override fun bind(reactor: CounterReactor): ReactorView<CounterReactor> {
    // Actions
    RxView.clicks(plusButton)
      .map { CounterReactor.Action.Increase }
      .bind(to = reactor.action)
      .disposed(by = disposeBag)

    RxView.clicks(minusButton)
      .map { CounterReactor.Action.Decrease }
      .bind(to = reactor.action)
      .disposed(by = disposeBag)

    // States
    reactor.state.map { it.value }
      .distinctUntilChanged()
      .map { "$it" }
      .bind(to = RxTextView.text(valueTextView))
      .disposed(by = disposeBag)

    reactor.state.map { it.isLoading }
      .distinctUntilChanged()
      .bind(to = RxView.visibility(progressBar, View.GONE))
      .disposed(by = disposeBag)

    return this
  }
}
