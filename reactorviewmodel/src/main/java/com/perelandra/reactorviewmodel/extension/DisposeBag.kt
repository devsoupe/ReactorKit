package com.perelandra.reactorviewmodel.extension

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

typealias DisposeBag = CompositeDisposable

fun <T : Disposable> T.disposed(by: DisposeBag) = by.add(this)