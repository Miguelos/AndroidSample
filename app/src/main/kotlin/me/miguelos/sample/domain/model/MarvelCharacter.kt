package me.miguelos.sample.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarvelCharacter(
    val id: Long = -1L,
    val name: String = "",
    val description: String = "",
    val thumbnail: Image = Image(),
    val urls: List<Url> = emptyList(),
    val availableComics: Int = 0
) : Parcelable
