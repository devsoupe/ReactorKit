package com.perelandra.sample.githubsearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.perelandra.sample.githubsearch.ui.main.GithubSearchFragment

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