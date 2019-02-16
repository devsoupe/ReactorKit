package com.perelandra.reactorkit

import com.perelandra.reactorkit.View.Companion.reactorKey

interface ReactorView<T : Reactor<*, *, *>> : View<T>, AssociatedObjectStore {

  override var reactor: T
    get() = associatedObject(reactorKey)
    set(value) {
      setAssociatedObject(value, reactorKey)
      isReactorBinded = false
      performBinding()
    }

  var isReactorBinded: Boolean
    get() = associatedObject(isReactorBindedKey, false)
    set(value) = setAssociatedObject(value, isReactorBindedKey)

  fun performBinding() {
    if (reactor == null) return
    if (isReactorBinded) return
    bind(reactor)
    isReactorBinded = true
  }

  fun createReactor(reactor: T) {
    this.reactor = reactor
  }

  fun destroyReactor() {
    reactor.clear()
    disposeBag.clear()
    clearAssociatedObject()
  }

  companion object {
    private const val isReactorBindedKey = "isReactorBinded"
  }
}
