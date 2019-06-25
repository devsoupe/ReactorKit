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
 * View 테스트
 *
 * 사용자 인터랙션이 발생했을 때 Action이 리액터로 잘 전달되는지
 * 리액터의 상태가 바뀌었을 때 뷰의 컴포넌트 속성이 잘 변경되는지
 *
 * (리액터의 stub 기능을 이용하면 뷰를 쉽게 테스트할 수 있습니다. stub 기능을 활성화하면 리액터가 받은 Action을 모두 기록하고, mutate()와 reduce()를 실행하는 대신 외부에서 상태를 설정할 수 있게 됩니다.)
 *
 */
class CounterViewTest {

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
  fun testAction_increase() {
    /**
     * given
     * Stub 리액터를 준비하고 뷰에 주입한다.
     */
    val reactor = CounterReactor().apply { stub.isEnabled = true }
    // TODO: View에 Reactor 주입

    /**
     * when
     * 플러스 버튼을 누른다.
     */
    // TODO: View에 플러스 버튼 클릭 인터랙션을 발생시킨다.

    /**
     * then
     * 리액터에 Action Increase가 잘 전달되었는지 확인한다.
     */
    assertEquals(reactor.stub.actions.last(), Increase)
  }

  /**
   * State isLoading 값이 true 인 경우 프로그래스바가 보여야 한다.
   */
  @Test
  fun testState_isLoading_visible() {
    /**
     * given
     * Stub 리액터를 준비하고 뷰에 주입한다.
     */
    val reactor = CounterReactor().apply { stub.isEnabled = true }
    // TODO: View에 Reactor 주입

    /**
     * when
     * 리액터 State의 isLoading 상태를 true로 변경한다.
     */
    reactor.stub.state.accept(CounterReactor.State(isLoading = true))

    /**
     * then
     * 프로그래스바 상태가 Visible 인지 확인한다.
     */
    // TODO: View 프로그래스바 상태 확인
  }

  /**
   * State isLoading 값이 false 인 경우 프로그래스바가 보이지 않아야 한다.
   */
  @Test
  fun testState_isLoading_gone() {
    /**
     * given
     * Stub 리액터를 준비하고 뷰에 주입한다.
     */
    val reactor = CounterReactor().apply { stub.isEnabled = true }
    // TODO: View에 Reactor 주입

    /**
     * when
     * 리액터 State의 isLoading 상태를 false로 변경한다.
     */
    reactor.stub.state.accept(CounterReactor.State(isLoading = false))

    /**
     * then
     * 프로그래스바 상태가 Gone 인지 확인한다.
     */
    // TODO: View 프로그래스바 상태 확인
  }
}