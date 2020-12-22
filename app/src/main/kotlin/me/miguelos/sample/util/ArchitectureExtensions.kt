@file:JvmName("ArchitectureExtensions")
package me.miguelos.sample.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData


fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, { it?.let { action(it) } })
}
