package com.perelandra.sample.counter

import com.perelandra.sample.counter.ui.main.CounterReactor
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CounterReactorTests {

  private lateinit var reactor: CounterReactor

  @Before
  fun setup() {
    RxJavaPlugins.setIoSchedulerHandler {
      Schedulers.trampoline()
    }

    RxAndroidPlugins.setInitMainThreadSchedulerHandler {
      Schedulers.trampoline()
    }

    reactor = CounterReactor().apply { state.subscribe() }
  }

  @After
  fun teardown() {
    RxJavaPlugins.reset()
    RxAndroidPlugins.reset()
  }

  @Test
  fun testActionIncrease_StateValuePlus1() {
    reactor.action.accept(CounterReactor.Action.Increase)
    Thread.sleep(1000)
    assertEquals(reactor.currentState.value, 1)
  }

  @Test
  fun testActionDecrease_StateValueMinus1() {
    reactor.action.accept(CounterReactor.Action.Decrease)
    Thread.sleep(1000)
    assertEquals(reactor.currentState.value, -1)
  }

  @Test
  fun testState_isLoading() {
    val reactor = CounterReactor()
    reactor.stub.isEnabled = true

    reactor.stub.state.accept(CounterReactor.State(isLoading = true))


  }

  @Test
  fun testAction_Increase() {
    // 1. Stub 리액터를 준비합니다.
    val reactor = CounterReactor()

    // 2. 리액터에 액션을 전달합니다.
    reactor.action.accept(CounterReactor.Action.Increase)


    // 3. 리액터의 상태가 변경되는지를 검증합니다.
    assertEquals(reactor.currentState.value, 1)
  }
}