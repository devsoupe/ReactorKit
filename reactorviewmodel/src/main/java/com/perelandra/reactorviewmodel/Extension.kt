package com.perelandra.reactorviewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

fun <T : Disposable> T.disposed(by: DisposeBag) = by.add(this)

fun <T> Observable<T>.bind(to: Consumer<in T>): Disposable = this.subscribe(to)
fun <T> Observable<T>.bind(to: Observer<in T>) = this.subscribe(to)

fun <T : ViewModel> T.of(fragment: Fragment): T = ViewModelProviders.of(fragment, createViewModel(this)).get(this.javaClass)
fun <T : ViewModel> T.of(fragmentActivity: FragmentActivity): T = ViewModelProviders.of(fragmentActivity, createViewModel(this)).get(this.javaClass)

private fun <T : ViewModel> createViewModel(model: T) = object : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T = model as T
}