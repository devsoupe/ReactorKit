package com.perelandrax.sample.githubsearch.rxevent

import com.jakewharton.rxrelay3.PublishRelay

/**
 * A special subject for Event. It only emits `.next` event.
 */
typealias EventPublisher<T> = PublishRelay<T>