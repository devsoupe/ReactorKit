package com.perelandra.reactorviewmodel

interface ReactorView<T> : AssociatedObjectStore {

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

  fun performBinding() {
    if (viewmodel == null) return
    if (isReactorBinded) return
    setAssociatedObject<ReactorView<T>>(bind(viewmodel = viewmodel), reactorViewKey)
    isReactorBinded = true
  }

  fun bind(viewmodel: T): ReactorView<T>

  fun clearReactorView() {
    disposeBag.clear();
    clearAssociatedObject(getAssociatedObject<ReactorView<T>>(reactorViewKey).id)
  }
}

private var viewmodelKey = "viewmodel"
private var disposeBagKey = "disposeBag"
private var isReactorBindedKey = "isReactorBinded"
private var reactorViewKey = "reactorView"

var <T> ReactorView<T>.isReactorBinded: Boolean
  get() = getAssociatedObject<Boolean>(key = isReactorBindedKey, default = false)
  set(value) { setAssociatedObject<Boolean>(value, isReactorBindedKey) }

