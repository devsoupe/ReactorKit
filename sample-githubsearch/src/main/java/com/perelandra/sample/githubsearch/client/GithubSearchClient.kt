package com.perelandra.sample.githubsearch.client

import io.reactivex.Observable

interface GithubSearchClient {

  fun request(url: String): Observable<String>
  fun cancel()
}