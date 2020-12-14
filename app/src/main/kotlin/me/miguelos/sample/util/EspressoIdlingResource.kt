package me.miguelos.sample.util

import androidx.test.espresso.IdlingResource


/**
 * Contains a static reference to [IdlingResource], only available in the 'mock' build type.
 */
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"
    private val DEFAULT_INSTANCE = SimpleCountingIdlingResource(RESOURCE)

    fun increment() {
        DEFAULT_INSTANCE.increment()
    }

    fun decrement() {
        DEFAULT_INSTANCE.decrement()
    }

    val idlingResource: IdlingResource
        get() = DEFAULT_INSTANCE
}
