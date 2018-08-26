package com.perelandra.sample.githubsearch.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buxikorea.buxi.library.reactorkit.ReactorView
import com.perelandra.reactorviewmodel.extension.of
import com.perelandra.sample.githubsearch.R

class GithubSearchFragment : Fragment(), ReactorView<GithubSearchViewModel> {

  companion object {
    fun newInstance() = GithubSearchFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.github_search_fragment, container, false)
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
