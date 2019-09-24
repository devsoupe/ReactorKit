package com.perelandrax.sample.githubsearch.ui.main

import com.perelandrax.reactorkit.rxevent.Event

data class GithubSearchQueryTextChangeEvent(
  val query: String = ""
) : Event