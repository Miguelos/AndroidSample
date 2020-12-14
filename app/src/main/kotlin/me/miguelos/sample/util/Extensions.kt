package me.miguelos.sample.util

val Any.TAG: String
    get() {
        val tag = this::class.simpleName ?: ""
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }
