package com.perelandra.sample.counter

import com.perelandra.sample.counter.ui.main.CounterReactor
import com.perelandra.sample.counter.ui.main.CounterReactor.Action.*
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
 * Action을 받았을때 원하는 State로 잘 변경되는지
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

  /**
   * Increase 액션이 발생할 경우 값을 1 증가시킨다.
   */
  @Test
  fun test_ActionIncreaseTrigger_ToStateValue1() {
    /**
     * given
     * 1. 리액터의 State value 값을 0으로 세팅한다.
     */
    val reactor = CounterReactor()
    reactor.initialState = CounterReactor.State(value = 0)

    /**
     * when
     * 2. 리액터에 Action Increase를 전달한다.
     */
    reactor.action.accept(Increase).apply { Thread.sleep(500) }

    /**
     * then
     * 3. 리액터 State의 value 값이 1이 되었는지 검증한다.
     */
    assertEquals(reactor.currentState.value, 1)
  }

  /**
   * Decrease 액션이 발생할 경우 값을 1 감소시킨다.
   */
  @Test
  fun test_ActionDecreaseTrigger_ToStateValueMinus1() {
    /**
     * given
     * 1. 리액터의 State value 값을 0으로 세팅한다.
     */
    val reactor = CounterReactor()
    reactor.initialState = CounterReactor.State(value = 0)

    /**
     * when
     * 2. 리액터에 Action Decrease를 전달한다.
     */
    reactor.action.accept(Decrease).apply { Thread.sleep(500) }

    /**
     * then
     * 3. 리액터 State의 value 값이 -1이 되었는지 검증한다.
     */
    assertEquals(reactor.currentState.value, -1)
  }
}