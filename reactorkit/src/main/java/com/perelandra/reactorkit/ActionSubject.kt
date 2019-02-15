package com.perelandra.reactorkit

import com.jakewharton.rxrelay2.PublishRelay

/**
 * A special subject for Reactor's Action. It only emits `.next` event.
 */
typealias ActionSubject<T> = PublishRelay<T>
