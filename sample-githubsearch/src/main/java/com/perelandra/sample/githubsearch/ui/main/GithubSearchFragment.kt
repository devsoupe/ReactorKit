package com.perelandra.sample.githubsearch.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.buxikorea.buxi.library.reactorkit.BaseReactorFragment
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.perelandra.reactorviewmodel.extension.bind
import com.perelandra.reactorviewmodel.extension.disposed
import com.perelandra.reactorviewmodel.extension.of
import com.perelandra.sample.githubsearch.R
import kotlinx.android.synthetic.main.fragment_github_search.*

class GithubSearchFragment : BaseReactorFragment<GithubSearchViewModel>() {

  companion object {
    fun newInstance() = GithubSearchFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_github_search, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    (activity as AppCompatActivity).setSupportActionBar(toolbar)

    list.layoutManager = LinearLayoutManager(context)
    list.adapter = GithubSearchListAdapter()

    setHasOptionsMenu(true)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    inflater?.inflate(R.menu.menu_toolbar, menu)

    val searchItem = menu?.findItem(R.id.action_search)
    val searchView = searchItem?.actionView as SearchView

    searchView.queryHint = "Search"
    RxSearchView.queryTextChanges(searchView)
      .skipInitialValue()
      .distinctUntilChanged()
      .filter { it.isNotEmpty() }
      .map { GithubSearchViewModel.Action.updateQuery(it.toString()) }
      .bind(to = viewModel.action)
      .disposed(by = disposeBag)

    Log.i(this::class.java.simpleName, "onCreateOptionsMenu : ${disposeBag}");
    Log.i(this::class.java.simpleName, "onCreateOptionsMenu : ${viewModel}");

    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun createViewModel(): GithubSearchViewModel = GithubSearchViewModel().of(this)

  override fun bindActions(viewModel: GithubSearchViewModel) {

  }

  override fun bindStates(viewModel: GithubSearchViewModel) {
    viewModel.state.map { it.query }
      .distinctUntilChanged()
      .filter { it.isNotEmpty() }
      .subscribe { Log.i(this::class.java.simpleName, "Query : $it")}
      .disposed(by = disposeBag)
  }
}
