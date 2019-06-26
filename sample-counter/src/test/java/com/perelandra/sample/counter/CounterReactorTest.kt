package com.perelandra.sample.counter

import com.perelandra.sample.counter.ui.main.CounterReactor
import com.perelandra.sample.counter.ui.main.CounterReactor.Action.Decrease
import com.perelandra.sample.counter.ui.main.CounterReactor.Action.Increase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Reactor 테스트
 *
 * Action을 받았을때 원하는 State로 잘 변경되는지
 *
 * (리액터는 뷰에 비해서 상대적으로 테스트하기 쉽습니다. Action이 전달되었을 때 비즈니스 로직을 수행하여 State가 바뀌는지를 확인하면 됩니다.)
 *
 */
class CounterReactorTest {

  @get:Rule
  val schedulersRule = SchedulersRule()

  @Before
  fun setUp() {

  }

  @After
  fun tearDown() {

  }

  // Increase 액션이 발생할 경우 값을 1 증가시킨다.
  @Test
  fun testValue_plus1() {
    // given
    // 리액터의 State value 값을 0으로 세팅한다.
    val reactor = CounterReactor()
    reactor.initialState = CounterReactor.State(value = 0)

    // when
    // 리액터에 Action Increase를 전달한다.
    reactor.action.accept(Increase).apply { Thread.sleep(500) }

    // then
    //리액터 State의 value 값이 1이 되었는지 검증한다.
    assertEquals(reactor.currentState.value, 1)
  }

  // Decrease 액션이 발생할 경우 값을 1 감소시킨다.
  @Test
  fun testValue_minus1() {
    // given
    // 리액터의 State value 값을 0으로 세팅한다.
    val reactor = CounterReactor()
    reactor.initialState = CounterReactor.State(value = 0)

    // when
    // 리액터에 Action Decrease를 전달한다.
    reactor.action.accept(Decrease).apply { Thread.sleep(500) }

    // then
    // 리액터 State의 value 값이 -1이 되었는지 검증한다.
    assertEquals(reactor.currentState.value, -1)
  }
}