package com.perelandra.reactorkit.rxevent

import com.jakewharton.rxrelay2.Relay
import com.perelandrax.rxevent.Event
import com.perelandrax.rxevent.EventPublisher
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 *
 */
object RxEvent {

  private val _publisher = EventPublisher.create<Event>().toSerialized()

  val publisher: Relay<Event> get() = _publisher

  fun publish(event: Event) {
    return publisher.accept(event)
  }

  fun <T> observe(eventType: Class<T>): Flowable<T> {
    return publisher.ofType(eventType).toFlowable(BackpressureStrategy.BUFFER)
  }

  // Using ofType operator to filter only match events class type
  inline fun <reified T : Event> observe(): Flowable<T> {
    return publisher.ofType(T::class.java).toFlowable(BackpressureStrategy.BUFFER)
  }
}