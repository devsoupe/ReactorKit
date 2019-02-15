package com.perelandra.reactorkit

import com.jakewharton.rxrelay2.BehaviorRelay

/**
 * StateRelay is a wrapper for `BehaviorSubject`.
 * Unlike `BehaviorSubject` it can't terminate with error or completed.
 */
typealias StateRelay<T> = BehaviorRelay<T>
