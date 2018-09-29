package com.perelandra.sample.githubsearch.client

import io.reactivex.Observable
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection

class GithubSearchClientImpl : GithubSearchClient {

  private val client = OkHttpClient()

  override fun request(url: String): Observable<String> {
    return Observable.create {
      val request = Request.Builder().url(url).build()
      val response = client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
          if (it.isDisposed) return
          it.onError(e)
        }

        override fun onResponse(call: Call, response: Response) {
          if (response.isSuccessful) {
            response.body()?.string()?.let { res ->
              if (it.isDisposed) return
              it.onNext(res)
              it.onComplete()
            }
          }

          if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
            if (it.isDisposed) return
            it.onError(Throwable("⚠️ GitHub API rate limit exceeded. Wait for 60 seconds and try again."))
          }
        }
      })
    }
  }

  override fun cancel() {
    for (call in client.dispatcher().queuedCalls()) call.cancel()
    for (call in client.dispatcher().runningCalls()) call.cancel()
  }
}