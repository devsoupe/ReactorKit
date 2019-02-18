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
}