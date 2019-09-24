package com.perelandrax.reactorkit.rxevent

import com.jakewharton.rxrelay2.PublishRelay

/**
 * A special subject for Event. It only emits `.next` event.
 */
typealias EventPublisher<T> = PublishRelay<T>