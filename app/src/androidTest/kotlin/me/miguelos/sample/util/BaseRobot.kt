package me.miguelos.sample.util

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import me.miguelos.sample.util.UITestExtensions.Companion.searchFor
import me.miguelos.sample.util.UITestExtensions.Companion.sleep
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

open class BaseRobot {

    fun doOnView(matcher: Matcher<View>, vararg actions: ViewAction) {
        actions.forEach {
            waitForView(matcher).perform(it)
        }
    }

    fun assertOnView(matcher: Matcher<View>, vararg assertions: ViewAssertion) {
        assertions.forEach {
            waitForView(matcher).check(it)
        }
    }

    fun fillEditText(resId: Int, text: String): ViewInteraction =
        onView(withId(resId)).perform(
            ViewActions.replaceText(text),
            ViewActions.closeSoftKeyboard()
        )

    fun fillEditTextAndApply(resId: Int, text: String): ViewInteraction =
        waitForView(withId(resId)).perform(
            ViewActions.replaceText(text),
            ViewActions.pressImeActionButton(),
            ViewActions.closeSoftKeyboard()
        )

    fun clickButton(resId: Int): ViewInteraction =
        waitForView((withId(resId))).perform(click())

    fun textView(resId: Int): ViewInteraction = waitForView(withId(resId))

    fun matchText(viewInteraction: ViewInteraction, text: String): ViewInteraction = viewInteraction
        .check(ViewAssertions.matches(withText(text)))

    fun matchText(resId: Int, text: String): ViewInteraction = matchText(textView(resId), text)

    fun clickListItem(listRes: Int, position: Int) {
        waitForView(withId(listRes))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position,
                    click()
                )
            )
    }

    fun clickNthView(listRes: Int, viewRes: Int, position: Int) {
        waitForView(
            allOf(
                withId(viewRes),
                isDescendantOfA(nthChildOf(withId(listRes), position))
            )
        ).perform(click())
    }

    fun dialogClickButton1() {
        waitForView(withId(android.R.id.button1)).perform(click())
    }

    fun dialogClickButton2() {
        waitForView(withId(android.R.id.button2)).perform(click())
    }

    fun turnOffInternetConnections() {
        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("svc data disable")
        sleep(1_000)
    }

    fun turnOnInternetConnections() {
        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("svc data enable")
        sleep(5_000)
    }

    /**
     * Perform action of implicitly waiting for a certain view.
     * This differs from EspressoExtensions.searchFor in that,
     * upon failure to locate an element, it will fetch a new root view
     * in which to traverse searching for our @param match
     *
     * @param viewMatcher ViewMatcher used to find our view
     */
    private fun waitForView(
        viewMatcher: Matcher<View>,
        waitMillis: Int = 5000,
        waitMillisPerTry: Long = 100
    ): ViewInteraction {

        // Derive the max tries
        val maxTries = waitMillis / waitMillisPerTry.toInt()

        var tries = 0

        for (i in 0..maxTries)
            try {
                // Track the amount of times we've tried
                tries++

                // Search the root for the view
                onView(isRoot()).perform(searchFor(viewMatcher))

                // If we're here, we found our view. Now return it
                return onView(viewMatcher)

            } catch (e: Exception) {

                if (tries == maxTries) {
                    throw e
                }
                sleep(waitMillisPerTry)
            }

        throw Exception("Error finding a view matching $viewMatcher")
    }

    private fun nthChildOf(
        parentMatcher: Matcher<View?>,
        childPosition: Int
    ): Matcher<View> = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("with $childPosition child view of type parentMatcher")
        }

        override fun matchesSafely(item: View): Boolean {
            if (item.parent !is ViewGroup) {
                return parentMatcher.matches(item.parent)
            }
            val group = item.parent as ViewGroup
            var view: View? = null
            if (parentMatcher.matches(item.parent)) {
                view = group.getChildAt(childPosition) as? ViewGroup
            }

            return view != null && view == item
        }
    }
}
