package me.miguelos.sample.util

import android.content.Context
import me.miguelos.sample.R
import timber.log.Timber
import java.net.UnknownHostException

/**
 * Factory used to create error messages from an Exception as a condition.
 */
object ErrorMessageFactory {

    fun create(context: Context, exception: Throwable): String {
        Timber.tag(TAG).e(exception)
        return with(context) {
            when (exception) {
                is UnknownHostException -> getString(R.string.exception_message_no_connection)
                else -> getString(R.string.exception_message_generic)
            }
        }
    }
}
