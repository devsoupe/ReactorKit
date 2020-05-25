package com.perelandrax.sample.githubsearch.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import com.perelandrax.reactorkit.ReactorView
import com.perelandrax.sample.githubsearch.R
import com.perelandrax.sample.githubsearch.rxevent.RxEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.fragment_github_search.*
import java.util.concurrent.TimeUnit

class GithubSearchFragment : Fragment(), ReactorView<GithubSearchReactor> {

  companion object {
    private val TAG = GithubSearchFragment::class.java.simpleName
    fun newInstance() = GithubSearchFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.fragment_github_search, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    (activity as AppCompatActivity).setSupportActionBar(toolbar)

    list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    list.layoutManager = LinearLayoutManager(context)
    list.adapter = GithubSearchListAdapter()

    setHasOptionsMenu(true)
    createReactor(GithubSearchReactor())
  }

  override fun onDestroyView() {
    super.onDestroyView()
    destroyReactor()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_toolbar, menu)

    val searchItem = menu.findItem(R.id.action_search)
    val searchView = searchItem.actionView as SearchView

    searchView.queryHint = "Search"

    searchView.queryTextChanges()
      .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
      .filter { it.isNotEmpty() }
      .subscribe { RxEvent.publish(GithubSearchQueryTextChangeEvent(it.toString())) }
      .addTo(disposables)

    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun bind(reactor: GithubSearchReactor) {
    // Actions
    RxEvent.observe<GithubSearchQueryTextChangeEvent>()
      .distinctUntilChanged()
      .map { GithubSearchReactor.Action.updateQuery(it.query) }
      .subscribe(reactor.action)
      .addTo(disposables)


    // States
    reactor.state.map { it.repos }
      .distinctUntilChanged()
      .subscribe { (list.adapter as GithubSearchListAdapter).setRepos(it) }
      .addTo(disposables)
  }
}
