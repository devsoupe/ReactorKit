package com.perelandrax.sample.githubsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perelandrax.sample.githubsearch.ui.main.GithubSearchFragment

class GithubSearchActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_github_search)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, GithubSearchFragment.newInstance())
        .commitNow()
    }
  }
}
