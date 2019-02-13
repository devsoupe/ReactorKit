package com.perelandra.reactorkit

class Stub<Action, Mutation, State> {

  public var isEnabled: Boolean = false

  public var state: StateRelay<State> = StateRelay.create()
  public var action: ActionSubject<Action> = ActionSubject.create()
}
