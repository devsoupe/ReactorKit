package com.perelandra.sample.githubsearch.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buxikorea.buxi.library.reactorkit.ReactorView
import com.perelandra.reactorviewmodel.extension.of
import com.perelandra.sample.githubsearch.R
import kotlinx.android.synthetic.main.github_search_fragment.*

class GithubSearchFragment : Fragment(), ReactorView<GithubSearchViewModel> {

  companion object {
    fun newInstance() = GithubSearchFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.github_search_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    (activity as AppCompatActivity).setSupportActionBar(toolbar)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    attachViewModel()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    detachViewModel()
  }

  override fun createViewModel(): GithubSearchViewModel = GithubSearchViewModel().of(this)

  override fun bindActions(viewModel: GithubSearchViewModel) {

  }

  override fun bindStates(viewModel: GithubSearchViewModel) {

  }

}
