package com.perelandrax.reactorkit

interface AssociatedObjectStore {

  val store: HashMap<String, Any>
    get() {
      if (stores[this] != null) {
        return stores[this]!!
      }
      val store: HashMap<String, Any> = HashMap()
      stores[this] = store
      return store
    }

  fun <T> associatedObject(key: String): T {
    return store[key] as T
  }

  fun <T> associatedObject(key: String, default: T): T {
    (store[key] as T?)?.let { return it }
    this.setAssociatedObject(default, key)
    return default
  }

  fun <T> setAssociatedObject(obj: T, key: String) {
    store[key] = obj as Any
  }

  fun clearAssociatedObject() {
    stores[this]?.let { it.clear() }
    stores.remove(this)
  }

  companion object {
    val stores: HashMap<Any, HashMap<String, Any>> = HashMap()
  }
}
