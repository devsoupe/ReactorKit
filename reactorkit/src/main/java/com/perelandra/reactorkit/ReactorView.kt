package com.perelandra.reactorkit

interface ReactorView<T> : AssociatedObjectStore {

  companion object {
    private const val reactorKey = "reactor"
    private const val disposeBagKey = "disposeBag"
    private const val isReactorBindedKey = "isReactorBinded"
    private const val reactorViewKey = "reactorView"
  }

  var disposeBag: DisposeBag
    get() = getAssociatedObject<DisposeBag>(disposeBagKey)
    set(value) {
      setAssociatedObject<DisposeBag>(value, disposeBagKey)
    }

  var reactor: T
    get() = getAssociatedObject<T>(reactorKey)
    set(value) {
      setAssociatedObject<T>(value, reactorKey)
      isReactorBinded = false
      disposeBag = DisposeBag()
      performBinding()
    }

  var <T> ReactorView<T>.isReactorBinded: Boolean
    get() = getAssociatedObject<Boolean>(isReactorBindedKey, false)
    set(value) {
      setAssociatedObject<Boolean>(value, isReactorBindedKey)
    }

  fun performBinding() {
    if (reactor == null) return
    if (isReactorBinded) return
    onBindAction(reactor)
    onBindState(reactor)
    setAssociatedObject<ReactorView<T>>(this, reactorViewKey)
    isReactorBinded = true
  }

  fun clearReactorView() {
    disposeBag.clear();
    clearAssociatedObject(getAssociatedObject<ReactorView<T>>(reactorViewKey).id)
  }

  fun onBindAction(reactor: T)

  fun onBindState(reactor: T)
}

