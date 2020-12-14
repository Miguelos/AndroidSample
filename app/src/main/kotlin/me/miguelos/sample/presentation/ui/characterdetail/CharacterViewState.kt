package me.miguelos.sample.presentation.ui.characterdetail

data class CharacterViewState(
    var isLoading: Boolean = false,
    var characterId: Long? = null
)
