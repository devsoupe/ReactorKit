package com.perelandra.reactorviewmodel.extension

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

fun <T : ViewModel> T.of(fragment: Fragment): T
  = ViewModelProviders.of(fragment, createViewModel(this)).get(this.javaClass)

fun <T : ViewModel> T.of(fragmentActivity: FragmentActivity): T
  = ViewModelProviders.of(fragmentActivity, createViewModel(this)).get(this.javaClass)

private fun <T : ViewModel> createViewModel(model: T) = object : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T = model as T
}