@file:JvmName("UIExtensions")
package me.miguelos.sample.util

import android.view.View
import com.google.android.material.snackbar.Snackbar


val Any.TAG: String
    get() {
        val tag = this::class.simpleName ?: "NoTag"
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun View.showSnackbar(s: String) {
    Snackbar.make(
        this,
        s,
        Snackbar.LENGTH_LONG
    ).show()
}
