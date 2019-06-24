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
 * 1. 사용자 인터랙션이 발생했을 때 Action이 리액터로 잘 전달되는지
 * 2. 리액터의 상태가 바뀌었을 때 뷰의 컴포넌트 속성이 잘 변경되는지
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
  fun testAction_isLoading() {
    // 1. Stub 리액터를 준비합니다.
    val reactor = CounterReactor().apply { stub.isEnabled = true }

    // 2. Stub된 리액터를 주입한 뷰를 준비합니다.

    // 3. 사용자 인터랙션을 발생시킵니다.
    reactor.stub.action.accept(Increase)

    // 4. 리액터에 액션이 잘 전달되었는지를 검증합니다.
    assertEquals(reactor.stub.actions.last(), Increase)
  }
}