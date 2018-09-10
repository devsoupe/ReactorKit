package com.perelandra.sample.githubsearch.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.buxikorea.buxi.library.reactorkit.ReactorView
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.view.RxView
import com.perelandra.reactorviewmodel.extension.bind
import com.perelandra.reactorviewmodel.extension.disposed
import com.perelandra.reactorviewmodel.extension.of
import com.perelandra.sample.githubsearch.R
import kotlinx.android.synthetic.main.fragment_github_search.*

class GithubSearchFragment : Fragment(), ReactorView<GithubSearchViewModel> {

  companion object {
    fun newInstance() = GithubSearchFragment()
  }

  private lateinit var searchView: SearchView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_github_search, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    (activity as AppCompatActivity).setSupportActionBar(toolbar)

    list.layoutManager = LinearLayoutManager(context)
    list.adapter = GithubSearchListAdapter()

    setHasOptionsMenu(true)
    attachViewModel()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    detachViewModel()
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    inflater?.inflate(R.menu.menu_toolbar, menu)

    val searchItem = menu?.findItem(R.id.action_search)
    searchView = searchItem?.actionView as SearchView

    searchView.queryHint = "Search"
//    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//
//      override fun onQueryTextSubmit(query: String?): Boolean = false
//
//      override fun onQueryTextChange(newText: String?): Boolean {
//        return true
//      }
//    })

    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun createViewModel(): GithubSearchViewModel = GithubSearchViewModel().of(this)

  override fun bindActions(viewModel: GithubSearchViewModel) {
    RxSearchView.queryTextChanges(searchView)
      .skipInitialValue()
      .map {
        Log.i(this::class.java.simpleName, "it.toString() : " + it.toString())
        GithubSearchViewModel.Action.updateQuery(it.toString())
      }
      .bind(to = viewModel.action)
      .disposed(by = disposeBag)
  }

  override fun bindStates(viewModel: GithubSearchViewModel) {

  }

}
