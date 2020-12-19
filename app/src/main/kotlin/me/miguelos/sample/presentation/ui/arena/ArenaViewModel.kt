package me.miguelos.sample.presentation.ui.arena

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import me.miguelos.sample.presentation.core.BaseViewModel
import me.miguelos.sample.presentation.core.SingleLiveEvent
import me.miguelos.sample.presentation.model.MarvelCharacter


class ArenaViewModel @ViewModelInject constructor() : BaseViewModel() {

    var viewState: MutableLiveData<ArenaViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable> = SingleLiveEvent()
    var charactersState: MutableLiveData<Pair<MarvelCharacter, MarvelCharacter>> = SingleLiveEvent()


    init {
        viewState.value = ArenaViewState(isLoading = false)
    }

}
