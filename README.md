# ReactorKit (Flux and Reactive Programming Architecture for Android)

<p align="center">
  <img alt="flow" src="https://cloud.githubusercontent.com/assets/931655/25277625/6aa05998-26da-11e7-9b85-e48bec938a6e.png">
</p>

[![ref](https://img.shields.io/static/v1.svg?label=AndroidStudio&message=3.5.1&color=blueviolet)](https://developer.android.com/studio)
[![ref](https://img.shields.io/badge/platform-android-lightgrey.svg)](https://developer.android.com/)
[![ref](https://img.shields.io/badge/Kotlin-1.3.50-orange.svg)](https://kotlinlang.org/)
[![ref](https://img.shields.io/badge/gradle--wrapper-gradle--5.4.1--all-yellowgreen.svg)](https://gradle.org/)
[![ref](https://img.shields.io/badge/gradle-3.5.1-blue.svg)](https://gradle.org/)
[![ref](https://jitpack.io/v/perelandrax/reactorkit.svg)](https://jitpack.io/#perelandrax/reactorkit)
[![ref](https://travis-ci.org/perelandrax/ReactorKit.svg?branch=master)](https://travis-ci.org/perelandrax/ReactorKit)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

🏠 <b>Port of [ReactorKit](https://github.com/ReactorKit/ReactorKit) to Kotlin, which corresponds to [ReactorKit/1.2.1](https://github.com/ReactorKit/ReactorKit/releases/tag/1.2.1)</b><br>
😬 <b>Please let me know if you need to fix this document or your license. Thank you, [Suyeol Jeon (devxoul)](https://github.com/devxoul)</b>

## Getting Started

<b>Please refer to original ReactorKit's</b> : [ReactorKit Documentation](https://github.com/ReactorKit/ReactorKit/blob/master/README.md) that describes the core components of apps built with ReactorKit. To get an understanding of the core principles we recommend reading the brilliant flux and reactive programming documentation.

ReactorKit is a framework for a reactive and unidirectional Kotlin application architecture. This repository introduces the basic concept of ReactorKit and describes how to build an application using ReactorKit. You may want to see the Examples section first if you'd like to see the actual code.

## Table of Contents

* [Basic Concept](#basic-concept)
  * [Design Goal](#design-goal)
  * [View](#view)
  * [Reactor](#reactor)
* [Advanced](#advanced)
  * [Global States](#global-states)
  * [View Communication](#view-communication)
  * [Testing](#testing)
* [Examples](#examples)
* [Installation](#installation)
* [License](#license)

## Basic Concept

ReactorKit is a combination of [Flux](https://facebook.github.io/flux/) and [Reactive Programming](https://en.wikipedia.org/wiki/Reactive_programming). The user actions and the view states are delivered to each layer via observable streams. These streams are unidirectional: the view can only emit actions and the reactor can only emit states.

<p align="center">
  <img alt="flow" src="https://cloud.githubusercontent.com/assets/931655/25073432/a91c1688-2321-11e7-8f04-bf91031a09dd.png" width="600">
</p>

### Design Goal

* **Testability**: The first purpose of ReactorKit is to separate the business logic from a view. This can make the code testable. A reactor doesn't have any dependency to a view. Just test reactors and test view bindings. See [Testing](#testing) section for details.
* **Start Small**: ReactorKit doesn't require the whole application to follow a single architecture. ReactorKit can be adopted partially, for one or more specific views. You don't need to rewrite everything to use ReactorKit on your existing project.
* **Less Typing**: ReactorKit focuses on avoiding complicated code for a simple thing. ReactorKit requires less code compared to other architectures. Start simple and scale up.

### View

A *View* displays data. A activity and fragment are treated as a view. The view binds user inputs to the action stream and binds the view states to each UI component. There's no business logic in a view layer. A view just defines how to map the action stream and the state stream.

To define a view, just have an existing class conform a interface named `ReactorView`. Then your class will have a property named `reactor` automatically. This property is typically set using createReactor method's parameter.

```kotlin
class ProfileFragment : Fragment(), ReactorView<ProfileReactor> {
  ...
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    createReactor(CounterReactor()) // inject reactor
  }

  override fun onDestroyView() {
    super.onDestroyView()
    destroyReactor()
  }
  ...  
}
```

When the `reactor` property has changed, `bind(reactor: <T : Reactor<*, *, *>>)` gets called. Implement this method to define the bindings of an action stream and a state stream.

```kotlin
override fun bind(reactor: ProfileReactor) {
  ...
  // Action (View -> Reactor)
  RxView.clicks(refreshButton)
      .map { ProfileReactor.Action.Refresh }
      .bind(to = reactor.action)
      .disposed(by = disposeBag)

  // State (Reactor -> View)
  reactor.state.map { it.isFollowing }
      .distinctUntilChanged()
      .bind(to = RxCompoundButton.checked(followButton))
      .disposed(by = disposeBag)
  ...
}
```

### Reactor

A *Reactor* is an UI-independent layer which manages the state of a view. The foremost role of a reactor is to separate control flow from a view. Every view has its corresponding reactor and delegates all logic to its reactor. A reactor has no dependency to a view, so it can be easily tested.

Conform to the `Reactor` interface to define a reactor. This interface requires three types to be defined: `Action`, `Mutation` and `State`. It also requires a property named `initialState`.

```kotlin
class ProfileReactor
  : Reactor<ProfileReactor.Action, ProfileReactor.Mutation, ProfileReactor.State> {
  ...
  override var initialState: State = State()
    
  // represent user actions
  sealed class Action {
    data class RefreshFollowingStatus(val userId: Int) : Action()
    data class Follow(val userId: Int) : Action()
  }

  // represent state changes
  sealed class Mutation {
    data class SetFollowing(val isFollowing: Boolean) : Mutation()
  }

  // represents the current view state
  data class State(
      val isFollowing: Boolean = false
  )
  ...
}
```

An `Action` represents a user interaction and `State` represents a view state. `Mutation` is a bridge between `Action` and `State`. A reactor converts the action stream to the state stream in two steps: `mutate()` and `reduce()`.

<p align="center">
  <img alt="flow-reactor" src="https://cloud.githubusercontent.com/assets/931655/25098066/2de21a28-23e2-11e7-8a41-d33d199dd951.png" width="800">
</p>

#### `mutate()`

`mutate()` receives an `Action` and generates an `Observable<Mutation>`.

```kotlin
override fun mutate(val action: Action): Observable<Mutation>
```

Every side effect, such as an async operation or API call, is performed in this method.

```kotlin
override fun mutate(action: Action): Observable<Mutation> = when (action) {
  is Action.RefreshFollowingStatus(userID) -> // receive an action
    UserAPI.isFollowing(userID) // create an API stream
      .map { isFollowing -> Mutation.setFollowing(isFollowing) } // convert to Mutation stream

  is Action.Follow(userID) -> 
    UserAPI.follow().map { Mutation.setFollowing(true) }
}
```

#### `reduce()`

`reduce()` generates a new `State` from a previous `State` and a `Mutation`.

```kotlin
override fun reduce(state: State, mutation: Mutation): State
```

This method is a pure function. It should just return a new `State` synchronously. Don't perform any side effects in this function.

```kotlin
override fun reduce(state: State, mutation: Mutation): State = when (mutation) {
  is Mutation.SetFollowing(isFollowing) -> state.copy(isFollowing = mutation.isFollowing) // manipulate the state, creating a new state
}
```

* ##### Jetpack Style (ViewModel + LiveData)

```kotlin
override fun reduce(state: State, mutation: Mutation): State = when (mutation) {
  is Mutation.SetFollowing(isFollowing) -> state.apply { isFollowing.value = mutation.isFollowing } // manipulate the state, applying a new state
}
```

#### `transform()`

`transform()` transforms each stream. There are three `transform()` functions:

```kotlin
override fun transformAction(action: Observable<Action>): Observable<Action>
override fun transformMutation(mutation: Observable<Mutation>): Observable<Mutation>
override fun transformState(state: Observable<State>): Observable<State>
```

Implement these methods to transform and combine with other observable streams. For example, `transform(mutation:)` is the best place for combining a global event stream to a mutation stream. See the [Global States](#global-states) section for details.

These methods can be also used for logging purposes:

```kotlin
override fun transformAction(action: Observable<Action>): Observable<Action> {
  return action.doOnNext { Log.i(TAG, "$it") } // Logging action event
}
```

## Advanced

### Global States

Unlike Redux, ReactorKit doesn't define a global app state. It means that you can use anything to manage a global state. You can use a `BehaviorSubject`, a `PublishSubject` or even a reactor. ReactorKit doesn't force to have a global state so you can use ReactorKit in a specific feature in your application.

There is no global state in the **Action → Mutation → State** flow. You should use `transformMutation(mutation: Observable<Mutation>)` to transform the global state to a mutation. Let's assume that we have a global `BehaviorSubject` which stores the current authenticated user. If you'd like to emit a `Mutation.setUser(val user: User?)` when the `currentUser` is changed, you can do as following:

```kotlin
var currentUser: BehaviorSubject<User> // global state

override fun transformMutation(mutation: Observable<Mutation>): Observable<Mutation> {
  return Observable.merge(mutation, currentUser.map(Mutation.setUser))
}
```

Then the mutation will be emitted each time the view sends an action to a reactor and the `currentUser` is changed.

### View Communication

* TBD

### Testing

ReactorKit has a built-in functionality for a testing. You'll be able to easily test both a view and a reactor with a following instruction.

#### What to test

First of all, you have to decide what to test. There are two things to test: a view and a reactor.

* View
  * Action: is a proper action sent to a reactor with a given user interaction?
  * State: is a view property set properly with a following state?
* Reactor
  * State: is a state changed properly with an action?

#### View testing

A view can be tested with a *stub* reactor. A reactor has a property `stub` which can log actions and force change states. If a reactor's stub is enabled, both `mutate()` and `reduce()` are not executed. A stub has these properties:

```kotlin
var isEnabled: Bool { get set }
val state: StateRelay<Reactor.State> { get }
val action: ActionSubject<Reactor.Action> { get }
val actions: MutableList<Action> { get } // recorded actions
```

Here are some example test cases:

```kotlin
fun testAction_refresh() {
  // 1. prepare a stub reactor
  val reactor = MyReactor().apply { stub.isEnabled = true }

  // 2. prepare a view with a stub reactor
  val fragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.container) as MyFragment
  InstrumentationRegistry.getInstrumentation().runOnMainSync { fragment.reactor = reactor }

  // 3. send an user interaction programatically (with ui test framework espresso)
  onView(withId(R.id.refreshControl)).perform(click())

  // 4. assert actions
  assertEquals(reactor.stub.actions.last(), Refresh)
}

fun testState_isLoading() {
  // 1. prepare a stub reactor
  val reactor = MyReactor().apply { stub.isEnabled = true }

  // 2. prepare a view with a stub reactor
  val fragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.container) as MyFragment
  InstrumentationRegistry.getInstrumentation().runOnMainSync { fragment.reactor = reactor }

  // 3. set a stub state
  reactor.stub.state.accept(MyReactor.State(isLoading = true))

  // 4. assert view properties (with ui test framework espresso)
  onView(withId(R.id.activityIndicator)).check(matches(isDisplayed()))
}
```

#### Reactor testing

A reactor can be tested independently.

```kotlin
fun testIsBookmarked() {
  val reactor = MyReactor()
  reactor.initialState = MyReactor.State(value = 0)

  reactor.action.accept(ToggleBookmarked)  
  assertEquals(reactor.currentState.isBookmarked, true)

  reactor.action.accept(ToggleBookmarked)  
  assertEquals(reactor.currentState.isBookmarked, false)
}
```

Sometimes a state is changed more than one time for a single action. For example, a `Refresh` action sets `state.isLoading` to `true` at first and sets to `false` after the refreshing.

## Examples

* [Counter](https://github.com/perelandrax/ReactorKit/tree/master/sample-counter) : The most simple and basic example of ReactorKit<!-- * [Counter-Jetpack](https://github.com/perelandrax/ReactorKit/tree/master/sample-counter-jetpack) : The most simple and basic example of ReactorKit -->
* [GitHub Search 🚧](https://github.com/perelandrax/ReactorKit/tree/master/sample-githubsearch) : A simple application which provides a GitHub repository search (Under Construction)

## Installation

ReactorKit officially supports JitPack only.

**JitPack**

Add the JitPack repository and dependency to your build file

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

```groovy
dependencies {
  implementation 'com.github.perelandrax:reactorkit:${version}'
}
```

## License
ReactorKit is under MIT license. See the LICENSE for more info.