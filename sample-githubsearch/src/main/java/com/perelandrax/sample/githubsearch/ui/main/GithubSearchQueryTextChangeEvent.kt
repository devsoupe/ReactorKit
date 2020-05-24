package com.perelandrax.sample.githubsearch.ui.main

import com.perelandrax.sample.githubsearch.rxevent.Event

data class GithubSearchQueryTextChangeEvent(
  val query: String = ""
) : Event