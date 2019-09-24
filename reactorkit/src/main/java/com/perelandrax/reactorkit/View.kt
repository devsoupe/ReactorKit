package com.perelandrax.reactorkit

import com.perelandrax.reactorkit.extras.DisposeBag

interface View<T : Reactor<*, *, *>> : AssociatedObjectStore {

  // A dispose bag. It is disposed each time the `reactor` is assigned.
  val disposeBag: DisposeBag
    get() = associatedObject(disposeBagKey, DisposeBag())

  // A view's reactor. `bind(reactor:)` gets called when the new value is assigned to this property.
  var reactor: T
    get() = associatedObject(reactorKey)
    set(value) {
      setAssociatedObject(value, reactorKey)
      bind(value)
    }

  // Creates RxKotlin bindings. This method is called each time the `reactor` is assigned.
  //
  // Here is a typical implementation example:
  //
  // ```
  // fun bind(reactor: MyReactor) {
  //   // Action
  //   RxView.clicks(increaseButton)
  //     .bind(to = Reactor.Action.Increase)
  //     .disposed(by = disposeBag)
  //
  //   // State
  //   reactor.state.map { it.count }
  //     .bind(to = RxTextView.text(countLabel))
  //     .disposed(by = disposeBag)
  // }
  // ```
  //
  // - warning: It's not recommended to call this method directly.
  fun bind(reactor: T)

  // Associated Object Keys
  companion object {
    const val reactorKey = "reactor"
    private const val disposeBagKey = "disposeBag"
  }
}
