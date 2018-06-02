package com.perelandra.reactorviewmodel.sample.counter.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.perelandra.reactorviewmodel.sample.counter.R

class CounterFragment : Fragment() {

  companion object {
    fun newInstance() = CounterFragment()
  }

  private lateinit var viewModel: CounterViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.counter_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(CounterViewModel::class.java)
    // TODO: Use the ViewModel
  }

}
