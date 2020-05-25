package com.perelandrax.reactorkit

import com.jakewharton.rxrelay3.PublishRelay

/**
 * A special subject for Reactor's Action. It only emits `.next` event.
 */
typealias ActionSubject<T> = PublishRelay<T>
