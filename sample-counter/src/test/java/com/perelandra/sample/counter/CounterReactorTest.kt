package com.perelandra.sample.counter

import com.perelandra.sample.counter.ui.main.CounterReactor
import com.perelandra.sample.counter.ui.main.CounterReactor.Action.Decrease
import com.perelandra.sample.counter.ui.main.CounterReactor.Action.Increase
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Reactor 테스트
 *
 * 1. Action을 받았을때 원하는 State로 잘 변경되는지
 *
 */
class CounterReactorTest {

  @Before
  fun setup() {
    RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
  }

  @After
  fun teardown() {
    RxJavaPlugins.reset()
    RxAndroidPlugins.reset()
  }

  @Test
  fun testIncrease() {
    // 1. 리액터를 준비합니다.
    val reactor = CounterReactor()

    // 2. 리액터에 액션을 전달합니다.
    reactor.action.accept(Increase).apply { Thread.sleep(500) }

    // 3. 리액터의 상태가 변경되는지를 검증합니다.
    assertEquals(reactor.currentState.value, 1)
  }

  @Test
  fun testDecrease() {
    // 1. 리액터를 준비합니다.
    val reactor = CounterReactor()

    // 2. 리액터에 액션을 전달합니다.
    reactor.action.accept(Decrease).apply { Thread.sleep(500) }

    // 3. 리액터의 상태가 변경되는지를 검증합니다.
    assertEquals(reactor.currentState.value, -1)
  }
}