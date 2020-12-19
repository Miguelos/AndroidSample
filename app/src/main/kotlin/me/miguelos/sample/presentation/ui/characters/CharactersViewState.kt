package me.miguelos.sample.presentation.ui.characters

data class CharactersViewState(
    var isLoading: Boolean = false,
    var query: String? = null
)