package com.perelandra.sample.counter

import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.perelandra.sample.counter.ui.main.CounterFragment
import com.perelandra.sample.counter.ui.main.CounterReactor
import com.perelandra.sample.counter.ui.main.CounterReactor.Action.Decrease
import com.perelandra.sample.counter.ui.main.CounterReactor.Action.Increase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

/**
 * View 테스트
 * (리액터의 stub 기능을 이용하면 뷰를 쉽게 테스트할 수 있습니다. stub 기능을 활성화하면 리액터가 받은 Action을 모두 기록하고, mutate()와 reduce()를 실행하는 대신 외부에서 상태를 설정할 수 있게 됩니다.)
 * - 사용자 인터랙션이 발생했을 때 Action이 리액터로 잘 전달되는지
 * - 리액터의 상태가 바뀌었을 때 뷰의 컴포넌트 속성이 잘 변경되는지
 *
 * @property activityRule ActivityTestRule<CounterActivity>
 */
@RunWith(AndroidJUnit4::class)
class CounterViewTest {

  @get:Rule
  val activityRule = ActivityTestRule(CounterActivity::class.java)

  @Before
  fun setUp() {
    ActivityScenario.launch(CounterActivity::class.java)
  }

  @After
  fun tearDown() {

  }

  /**
   * 플러스 버튼을 누른경우 Increase Action이 전달되어야 한다.
   */
  @Test
  fun testAction_givenReactorStub_whenPerformClickPlusButton_thenShouldActionsLastIncrease() {
    // Stub 리액터를 준비하고 뷰에 주입한다.
    val fragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.container) as CounterFragment
    val reactor = CounterReactor().apply { stub.isEnabled = true }
    InstrumentationRegistry.getInstrumentation().runOnMainSync { fragment.reactor = reactor }

    // 플러스 버튼을 누른다.
    onView(withId(R.id.plusButton)).perform(click())

    // 리액터에 Action Increase가 잘 전달되었는지 확인한다.
    assertEquals(reactor.stub.actions.last(), Increase)
  }

  /**
   * 마이너스 버튼을 누른경우 Decrease Action이 전달되어야 한다.
   */
  @Test
  fun testAction_givenReactorStub_whenPerformClickMinusButton_thenShouldActionsLastDecrease() {
    // Stub 리액터를 준비하고 뷰에 주입한다.
    val fragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.container) as CounterFragment
    val reactor = CounterReactor().apply { stub.isEnabled = true }
    InstrumentationRegistry.getInstrumentation().runOnMainSync { fragment.reactor = reactor }

    // 마이너스 버튼을 누른다.
    onView(withId(R.id.minusButton)).perform(click())

    // 리액터에 Action Increase가 잘 전달되었는지 확인한다.
    assertEquals(reactor.stub.actions.last(), Decrease)
  }

  /**
   * State count 값이 1로 변경된 경우 화면값이 1로 변경되어야 한다.
   */
  @Test
  fun testView_givenReactorStub_whenStateValue1_thenShouldViewValuePlus1() {
    // Stub 리액터를 준비하고 뷰에 주입한다.
    val reactor = CounterReactor().apply { stub.isEnabled = true }
    val fragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.container) as CounterFragment
    InstrumentationRegistry.getInstrumentation().runOnMainSync { fragment.reactor = reactor }

    // State count 값을 1로 변경한다.
    reactor.stub.state.accept(CounterReactor.State(count = 1))

    // 뷰의 Value Text 값이 1로 변경되는지 확인한다.
    onView(withId(R.id.valueTextView)).check(matches(withText("1")))
  }

  /**
   * State count 값이 -1로 변경된 경우 화면값이 -1로 변경되어야 한다.
   */
  @Test
  fun testView_givenReactorStub_whenStateValue1_thenShouldViewValueMinus1() {
    // Stub 리액터를 준비하고 뷰에 주입한다.
    val reactor = CounterReactor().apply { stub.isEnabled = true }
    val fragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.container) as CounterFragment
    InstrumentationRegistry.getInstrumentation().runOnMainSync { fragment.reactor = reactor }

    // State count 값을 -1로 변경한다.
    reactor.stub.state.accept(CounterReactor.State(count = -1))

    // 뷰의 Value Text 값이 -1로 변경되는지 확인한다.
    onView(withId(R.id.valueTextView)).check(matches(withText("-1")))
  }

  /**
   * State isLoading 값이 true인 경우 프로그래스바가 보여야 한다.
   */
  @Test
  fun testView_givenReactorStub_whenStateIsLoadingTrue_thenShouldViewProgressBarVisible() {
    // Stub 리액터를 준비하고 뷰에 주입한다.
    val reactor = CounterReactor().apply { stub.isEnabled = true }
    val fragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.container) as CounterFragment
    InstrumentationRegistry.getInstrumentation().runOnMainSync { fragment.reactor = reactor }

    // Espresso에서 ProgressBar 테스트를 위해 indeterminateDrawable의 에니메이션을 없앤다.
    val notAnimatedDrawable = ContextCompat.getDrawable(activityRule.activity, android.R.color.transparent)
    (activityRule.activity.findViewById(R.id.progressBar) as ProgressBar).indeterminateDrawable = notAnimatedDrawable

    // 리액터 State의 isLoading 상태를 true로 변경한다.
    reactor.stub.state.accept(CounterReactor.State(isLoading = true))

    // 프로그래스바 상태가 Visible 인지 확인한다.
    onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
  }

  /**
   * State isLoading 값이 false인 경우 프로그래스바가 보이지 않아야 한다.
   */
  @Test
  fun testView_givenReactorStub_whenStateIsLoadingFalse_thenShouldViewProgressBarGone() {
    // Stub 리액터를 준비하고 뷰에 주입한다.
    val reactor = CounterReactor().apply { stub.isEnabled = true }
    val fragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.container) as CounterFragment
    InstrumentationRegistry.getInstrumentation().runOnMainSync { fragment.reactor = reactor }

    // Espresso에서 ProgressBar 테스트를 위해 indeterminateDrawable의 에니메이션을 없앤다.
    val notAnimatedDrawable = ContextCompat.getDrawable(activityRule.activity, android.R.color.transparent)
    (activityRule.activity.findViewById(R.id.progressBar) as ProgressBar).indeterminateDrawable = notAnimatedDrawable

    // 리액터 State의 isLoading 상태를 false로 변경한다.
    reactor.stub.state.accept(CounterReactor.State(isLoading = false))

    // 프로그래스바 상태가 Visible 인지 확인한다.
    onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(Visibility.GONE)))
  }
}