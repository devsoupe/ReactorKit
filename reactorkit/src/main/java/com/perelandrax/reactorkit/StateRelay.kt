package com.perelandrax.reactorkit

import com.jakewharton.rxrelay3.BehaviorRelay

/**
 * StateRelay is a wrapper for `BehaviorSubject`.
 * Unlike `BehaviorSubject` it can't terminate with error or completed.
 */
typealias StateRelay<T> = BehaviorRelay<T>
