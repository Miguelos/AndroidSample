package me.miguelos.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import me.miguelos.sample.util.UITestExtensions.Companion.PACKAGE_NAME
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext = getInstrumentation().targetContext
        assertEquals(PACKAGE_NAME, appContext.packageName)
    }
}
