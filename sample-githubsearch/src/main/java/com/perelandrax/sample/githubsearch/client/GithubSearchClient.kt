package com.perelandrax.sample.githubsearch.client

import io.reactivex.rxjava3.core.Observable

interface GithubSearchClient {

  fun request(url: String): Observable<String>
  fun cancel()
}