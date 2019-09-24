package com.perelandrax.sample.githubsearch.client

import android.util.Log
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.HttpURLConnection

class GithubSearchClientImpl : GithubSearchClient {

  private val client = OkHttpClient.Builder().apply {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    addInterceptor(logging)
  }.build()

  override fun request(url: String): Observable<String> {
    return Observable.defer {
      try {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
          return@defer Observable.just(response.body()?.string())
        }

        if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
          throw Throwable("⚠️ GitHub API rate limit exceeded. Wait for 60 seconds and try again.")
        } else {
          throw Throwable("⚠️ GitHub API error code : ${response.code()}")
        }
      } catch (e: IOException) {
        cancel()
        return@defer null
      }
    }
  }

  override fun cancel() {
    Log.i("SETH_DEBUG", "cancel")
    for (call in client.dispatcher().queuedCalls()) call.cancel()
    for (call in client.dispatcher().runningCalls()) call.cancel()
  }
}