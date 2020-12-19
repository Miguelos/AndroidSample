package me.miguelos.sample

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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

    private var robot = BaseRobot()

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
        robot.assertOnView(withId(R.id.toolbar))
        robot.assertOnView(withId(R.id.list_button))
        robot.assertOnView(withId(R.id.arena_button))
        robot.assertOnView(withId(R.id.ranking_button))
    }

    @Test
    fun searchList() {
        robot.turnOnInternetConnections()
        robot.clickButton(R.id.list_button)
        robot.fillEditTextAndApply(R.id.search_et, "loki")
        robot.doOnView(withText("Loki"), ViewActions.click())
        robot.assertOnView(withId(R.id.character_cl))
    }

    /**
     * Check that without Internet connection an alert is shown to the user when searching for
     * characters.
     */
    @Test
    fun arenaFightHappyPath() {
        robot.turnOnInternetConnections()
        robot.clickButton(R.id.arena_button)
        robot.clickButton(R.id.select_fighters_b)
        robot.fillEditTextAndApply(R.id.search_et, "lo")
        robot.clickNthView(R.id.characters_rv, R.id.list_item_cb, 0)
        robot.clickNthView(R.id.characters_rv, R.id.list_item_cb, 1)
        robot.clickButton(R.id.select_list_items_b)
        robot.clickButton(R.id.fight_b)
        robot.turnOffInternetConnections()
        robot.clickButton(R.id.go_to_ranking_b)
        robot.assertOnView(withId(R.id.list_item_name_tv))
        robot.turnOnInternetConnections()
    }

    /**
     * Check that without Internet the Ranking shows previous characters involved in battles
     */
    @Test
    fun offlineSearchList() {
        robot.turnOffInternetConnections()
        robot.clickButton(R.id.list_button)
        robot.fillEditTextAndApply(R.id.search_et, "loki")
        robot.assertOnView(withText(R.string.dialog_offline))
        robot.turnOnInternetConnections()
    }
}
