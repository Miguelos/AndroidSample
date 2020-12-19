package me.miguelos.sample.presentation.ui.arena

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.schedulers.Schedulers
import me.miguelos.sample.common.TwoWaysMapper
import me.miguelos.sample.domain.usecase.SaveCharacters
import me.miguelos.sample.presentation.core.BaseViewModel
import me.miguelos.sample.presentation.core.SingleLiveEvent
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.domain.model.MarvelCharacter as DomainMarvelCharacter


class ArenaViewModel @ViewModelInject constructor(
    private val saveCharacters: SaveCharacters,
    private val characterMapper: TwoWaysMapper<DomainMarvelCharacter, MarvelCharacter>
) : BaseViewModel() {

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
        performSaveCharacters(characterMapper.inverseMapFrom(it.toList()).toList())
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

    private fun performSaveCharacters(characters: List<DomainMarvelCharacter>) {
        addDisposable(saveCharacters.execute(
            SaveCharacters.RequestValues(
                marvelCharacters = characters,
                ranking = true
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _ ->
                viewState.value = viewState.value?.copy(
                    isLoading = false
                )
            },
                {
                    handleError(it)
                    viewState.value = viewState.value?.copy(
                        isLoading = false
                    )
                }
            )
        )
    }

    private fun handleError(it: Throwable) {
        if (it is CompositeException) {
            errorState.postValue(it.exceptions.first())
        } else {
            errorState.postValue(it)
        }
    }
}
