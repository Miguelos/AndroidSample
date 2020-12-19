package me.miguelos.sample.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarvelCharacter(
    val id: Long = -1L,
    val name: String = "",
    val description: String = "",
    val thumbnail: String = "",
    val urls: List<String> = emptyList(),
    val availableComics: Int = 0
) : Parcelable, Comparable<MarvelCharacter> {

    override fun compareTo(other: MarvelCharacter) = when {
        id < other.id -> {
            -1
        }
        id > other.id -> {
            1
        }
        else -> {
            0
        }
    }
}
