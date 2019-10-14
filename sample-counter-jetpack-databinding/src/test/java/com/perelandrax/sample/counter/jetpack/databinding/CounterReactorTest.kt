package com.perelandra.sample.counter.databinding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.perelandra.sample.counter.jetpack.databinding.ui.main.CounterReactor
import com.perelandra.sample.counter.jetpack.databinding.ui.main.CounterReactor.Action.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.test.assertEquals

/**
 * Reactor 테스트
 * (리액터는 뷰에 비해서 상대적으로 테스트하기 쉽습니다. Action이 전달되었을 때 비즈니스 로직을 수행하여 State가 바뀌는지를 확인하면 됩니다.)
 * - Action을 받았을때 원하는 State로 잘 변경되는지
 *
 * @property schedulersRule SchedulersRule Rx 테스트를 위해 Rule을 설정한다.
 * @property instantTaskExecutorRule InstantTaskExecutorRule liveData 테스트를 위해 Rule을 설정한다.
 */
class CounterReactorTest {

  @get:Rule
  val schedulersRule = SchedulersRule()

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var reactor: CounterReactor

  @Before
  fun setUp() {
    // 리액터를 초기값(State count 0)으로 생성한다.
    reactor = CounterReactor().apply { currentState.count.testObserver() }
  }

  @After
  fun tearDown() {

  }

  /**
   * Increase 액션이 발생할 경우 값을 1 증가시킨다.
   */
  @Test
  fun testState_givenStateValue0_whenActionIncrease_thenShouldStateValuePlus1() {
    // 리액터에 Action Increase를 전달한다.
    reactor.action.accept(Increase).apply { Thread.sleep(500) }

    // 리액터 State의 liveData count 값이 1이 되었는지 검증한다.
    assertEquals(reactor.currentState.count.value, 1)
  }

  /**
   * Decrease 액션이 발생할 경우 값을 1 감소시킨다.
   */
  @Test
  fun testState_givenStateValue0_whenActionDecrease_thenShouldStateValueMinus1() {
    // 리액터에 Action Decrease를 전달한다.
    reactor.action.accept(Decrease).apply { Thread.sleep(500) }

    // 리액터 State의 liveData count 값이 -1이 되었는지 검증한다.
    assertEquals(reactor.currentState.count.value, -1)
  }

  /**
   * Rx 테스트를 위해 스케줄러를 세팅하는 클래스
   */
  inner class SchedulersRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
      return object : Statement() {

        @Throws(Throwable::class)
        override fun evaluate() {
          RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
          RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
          RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
          RxJavaPlugins.setSingleSchedulerHandler { Schedulers.trampoline() }
          RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

          try {
            base.evaluate()
          } finally {
            RxJavaPlugins.reset()
            RxAndroidPlugins.reset()
          }
        }
      }
    }
  }

  /**
   * liveData 테스트를 위해 테스트 Observer를 세팅하는 클래스
   */
  inner class TestObserver<T> : Observer<T> {

    private val observedValues = mutableListOf<T?>()

    override fun onChanged(value: T?) {
      observedValues.add(value)
    }
  }

  private fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
  }
}