package com.perelandra.sample.githubsearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.perelandra.sample.githubsearch.ui.main.GithubSearchFragment
import com.perelandra.sample.githubsearch.R

class GithubSearchActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.github_search_activity)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, GithubSearchFragment.newInstance())
        .commitNow()
    }
  }
}
