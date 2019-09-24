package com.perelandrax.reactorkit.extras

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

fun <T : Disposable> T.disposed(by: DisposeBag) = by.add(this)

fun <T> Observable<T>.bind(to: Consumer<in T>): Disposable = this.subscribe(to, Consumer { it.printStackTrace() })

fun <T> Observable<T>.bind(to: Observer<in T>) = this.subscribe(to)

fun <T> Flowable<T>.bind(to: Consumer<in T>): Disposable = this.subscribe(to, Consumer { it.printStackTrace() })

fun <T : ViewModel> T.of(fragment: Fragment): T =
    ViewModelProviders.of(fragment, createViewModel(this)).get(this.javaClass)

fun <T : ViewModel> T.of(fragmentActivity: FragmentActivity): T =
    ViewModelProviders.of(fragmentActivity, createViewModel(this)).get(this.javaClass)

private fun <T : ViewModel> createViewModel(model: T) = object : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T = model as T
}