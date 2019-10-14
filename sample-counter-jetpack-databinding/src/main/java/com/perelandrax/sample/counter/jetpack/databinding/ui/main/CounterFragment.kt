package com.perelandra.sample.counter.jetpack.databinding.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding2.view.RxView
import com.perelandra.reactorkit.ReactorView
import com.perelandra.reactorkit.extras.bind
import com.perelandra.reactorkit.extras.disposed
import com.perelandra.reactorkit.extras.of
import com.perelandra.sample.counter.jetpack.databinding.R
import com.perelandra.sample.counter.jetpack.databinding.ui.main.CounterReactor.Action.*
import kotlinx.android.synthetic.main.fragment_counter.*

class CounterFragment : Fragment(), ReactorView<CounterReactor> {

  companion object {
    fun newInstance() = CounterFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
      inflater.inflate(R.layout.fragment_counter, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    createReactor(CounterReactor().of(this))
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
    reactor.state.take(1)
        .subscribe { state ->
          state.count.observe(this, Observer { value ->
            valueTextView.text = "$value"
          })

          state.isLoading.observe(this, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
          })
        }
        .disposed(by = disposeBag)
  }
}
