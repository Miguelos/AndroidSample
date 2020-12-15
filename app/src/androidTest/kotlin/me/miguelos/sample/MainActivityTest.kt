package me.miguelos.sample

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import me.miguelos.sample.presentation.ui.MainActivity
import me.miguelos.sample.util.BaseRobot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Tests to verify that the behavior of {@link MainActivity} is correct.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Rule
    @JvmField
    var hiltEmulatorTestRule = HiltAndroidRule(this)

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    var robot = BaseRobot()

    @Before
    fun init() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun restartActivity() {
        activityRule.scenario.apply {
            moveToState(Lifecycle.State.RESUMED)
            activityRule.scenario.recreate()
        }
    }

    @Test
    fun mainActivity() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.characters_cl)).check(matches(isDisplayed()))
    }

    @Test
    fun itemClick() {
        robot.doOnView(withId(R.id.characters_rv), click())
        robot.assertOnView(withId(R.id.character_cl))
    }
}
