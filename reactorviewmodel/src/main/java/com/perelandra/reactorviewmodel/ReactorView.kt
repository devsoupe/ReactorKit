package com.perelandra.reactorviewmodel

interface ReactorView<T> : AssociatedObjectStore {

  companion object {
    private const val viewmodelKey = "viewmodel"
    private const val disposeBagKey = "disposeBag"
    private const val isReactorBindedKey = "isReactorBinded"
    private const val reactorViewKey = "reactorView"
  }

  var disposeBag: DisposeBag
    get() = getAssociatedObject<DisposeBag>(disposeBagKey)
    set(value) {
      setAssociatedObject<DisposeBag>(value, disposeBagKey)
    }

  var viewmodel: T
    get() = getAssociatedObject<T>(viewmodelKey)
    set(value) {
      setAssociatedObject<T>(value, viewmodelKey)
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
    if (viewmodel == null) return
    if (isReactorBinded) return
    setAssociatedObject<ReactorView<T>>(bind(viewmodel), reactorViewKey)
    isReactorBinded = true
  }

  fun bind(viewmodel: T): ReactorView<T>

  fun clearReactorView() {
    disposeBag.clear();
    clearAssociatedObject(getAssociatedObject<ReactorView<T>>(reactorViewKey).id)
  }
}

