package com.perelandra.reactorkit

import com.perelandra.reactorkit.extras.DisposeBag

interface View<T : Reactor<*, *, *>> : AssociatedObjectStore {

  /**
   * A dispose bag. It is disposed each time the `reactor` is assigned.
   */
  val disposeBag: DisposeBag
    get() = associatedObject(disposeBagKey, DisposeBag())

  /**
   * A view's reactor. `bind(reactor:)` gets called when the new value is assigned to this property.
   */
  var reactor: T
    get() = associatedObject(reactorKey)
    set(value) {
      setAssociatedObject(value, reactorKey)
      bind(value)
    }

  fun bind(reactor: T)

  companion object {
    const val reactorKey = "reactor"
    private const val disposeBagKey = "disposeBag"
  }
}
