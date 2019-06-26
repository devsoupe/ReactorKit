package com.perelandra.reactorkit.extras

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

fun <T : Disposable> T.disposed(by: DisposeBag) = by.add(this)

fun <T> Observable<T>.bind(to: Consumer<in T>): Disposable = this.subscribe(to, Consumer { it.printStackTrace() })
