package com.perelandra.sample.counter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perelandra.sample.counter.ui.main.CounterFragment

class CounterActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_counter)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container, CounterFragment.newInstance())
          .commitNow()
    }
  }
}
