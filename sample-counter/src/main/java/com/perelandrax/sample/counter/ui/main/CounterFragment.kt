package com.perelandrax.sample.counter.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.visibility
import com.perelandrax.reactorkit.ReactorView
import com.perelandrax.reactorkit.viewmodel.debug
import com.perelandrax.sample.counter.R
import com.perelandrax.sample.counter.ui.main.CounterReactor.Action.Decrease
import com.perelandrax.sample.counter.ui.main.CounterReactor.Action.Increase
import io.reactivex.rxjava3.kotlin.addTo
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
    plusButton.clicks()
      .debug("plusButton.clicks")
      .map { Increase }
      .subscribe(reactor.action)
      .addTo(disposables)

    minusButton.clicks()
      .debug("minusButton.clicks")
      .map { Decrease }
      .subscribe(reactor.action)
      .addTo(disposables)

    // State
    reactor.state.map { it.count }
      .distinctUntilChanged()
      .debug("reactor.state.count")
      .map { "$it" }
      .subscribe(valueTextView::setText)
      .addTo(disposables)

    reactor.state.map { it.isLoading }
      .distinctUntilChanged()
      .debug("reactor.state.isLoading")
      .subscribe(progressBar.visibility())
      .addTo(disposables)
  }
}
