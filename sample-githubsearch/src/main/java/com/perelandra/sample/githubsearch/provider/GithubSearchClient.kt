package com.perelandra.sample.githubsearch.provider

import io.reactivex.Observable

interface GithubSearchClient {

  fun request(url: String): Observable<String>
  fun cancel()
}