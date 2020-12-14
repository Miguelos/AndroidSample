package me.miguelos.sample.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Url(
    val type: String = "",
    val url: String = ""
) : Parcelable
