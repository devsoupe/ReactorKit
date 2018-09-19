package com.perelandra.reactorviewmodel

import android.util.Log

interface AssociatedObjectStore {

  companion object {
    private val objectStores: HashMap<String, HashMap<String, Any>> = HashMap()
  }

  private val associatedObjectStore: HashMap<String, Any>
    get() {
      if (objectStores[id] != null) {
        return (objectStores[id] as HashMap<String, Any>)
      }
      val store: HashMap<String, Any> = HashMap()
      objectStores[id] = store
      return store
    }

  fun <T> associatedObject(key: String): T {
    return associatedObjectStore[key] as T
  }

  fun <T> associatedObject(key: String, default: T): T {
    (associatedObjectStore[key] as T?)?.let {
      return it
    }
    this.setAssociatedObject(default, key)
    return default
  }

  fun <T> setAssociatedObject(obj: T, key: String) {
    associatedObjectStore[key] = obj as Any
  }

  fun clearAssociatedObject(id: String) {
    Log.d("TEST", "clearAssociatedObject : $id")

    objectStores[id]?.let { it.clear() }
    objectStores.remove(id)
  }
}

val AssociatedObjectStore.id
  get() = "${this::class.java.simpleName}_${this::class.java.hashCode()}"
