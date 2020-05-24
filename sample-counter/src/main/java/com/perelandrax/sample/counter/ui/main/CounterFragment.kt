package com.perelandrax.sample.counter.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.perelandrax.reactorkit.ReactorView
import com.perelandrax.reactorkit.extras.bind
import com.perelandrax.reactorkit.extras.disposed
import com.perelandrax.sample.counter.R
import com.perelandrax.sample.counter.ui.main.CounterReactor.Action.Decrease
import com.perelandrax.sample.counter.ui.main.CounterReactor.Action.Increase
import kotlinx.android.synthetic.main.fragment_counter.*

class CounterFragment : Fragment(), ReactorView<CounterReactor> {

  companion object {
    fun newInstance() = CounterFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.fragment_counter, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    createReactor(CounterReactor())
  }

  override fun onDestroyView() {
    super.onDestroyView()
    destroyReactor()
  }

  override fun bind(reactor: CounterReactor) {
    // Action
    RxView.clicks(plusButton)
      .map { Increase }
      .bind(to = reactor.action)
      .disposed(by = disposeBag)

    RxView.clicks(minusButton)
      .map { Decrease }
      .bind(to = reactor.action)
      .disposed(by = disposeBag)

    // State
    reactor.state.map { it.count }
      .distinctUntilChanged()
      .map { "$it" }
      .bind(to = RxTextView.text(valueTextView))
      .disposed(by = disposeBag)

    reactor.state.map { it.isLoading }
      .distinctUntilChanged()
      .bind(to = RxView.visibility(progressBar, View.GONE))
      .disposed(by = disposeBag)
  }
}
