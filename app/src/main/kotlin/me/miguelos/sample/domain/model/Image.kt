package me.miguelos.sample.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val imageUrl: String = ""
) : Parcelable
