package me.miguelos.sample.presentation.ui.arena

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import me.miguelos.sample.presentation.core.BaseViewModel
import me.miguelos.sample.presentation.core.SingleLiveEvent
import me.miguelos.sample.presentation.model.MarvelCharacter


class ArenaViewModel @ViewModelInject constructor() : BaseViewModel() {

    var viewState: MutableLiveData<ArenaViewState> = MutableLiveData()
    var charactersState: MutableLiveData<Pair<MarvelCharacter, MarvelCharacter>> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable> = SingleLiveEvent()

    init {
        viewState.value = ArenaViewState(isLoading = false)
    }

    fun saveFighters(first: MarvelCharacter, second: MarvelCharacter) {
        charactersState.value = Pair(first, second)
    }

    fun getWinner() = charactersState.value?.let {
        when {
            it.first.availableComics > it.second.availableComics -> {
                it.first
            }
            it.first.availableComics < it.second.availableComics -> {
                it.second
            }
            else -> {
                null
            }
        }
    }
}
