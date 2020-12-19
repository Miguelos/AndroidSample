package me.miguelos.sample.presentation.ui.characterdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.schedulers.Schedulers
import me.miguelos.sample.common.TwoWaysMapper
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.presentation.core.BaseViewModel
import me.miguelos.sample.presentation.core.SingleLiveEvent
import me.miguelos.sample.presentation.model.MarvelCharacter
import timber.log.Timber
import me.miguelos.sample.domain.model.MarvelCharacter as DomainMarvelCharacter

class CharacterDetailViewModel @ViewModelInject constructor(
    private val getCharacter: GetCharacter,
    private val characterMapper: TwoWaysMapper<DomainMarvelCharacter, MarvelCharacter>
) : BaseViewModel() {

    var viewState: MutableLiveData<CharacterViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable> = SingleLiveEvent()
    var characterState: MutableLiveData<MarvelCharacter> = SingleLiveEvent()


    init {
        viewState.value = CharacterViewState(isLoading = false)
    }

    fun saveId(id: Long) {
        viewState.value = viewState.value?.copy(characterId = id)
        getCharacter()
    }

    private fun getCharacter() {
        if (viewState.value?.isLoading == false && validateData()) {
            viewState.value = viewState.value?.copy(isLoading = true)
            viewState.value?.characterId?.let { performGetCharacterList(it) }
        }
    }

    private fun validateData() = viewState.value?.characterId != null

    private fun performGetCharacterList(id: Long) {
        Timber.tag(javaClass.simpleName).i("Getting characters list")

        addDisposable(getCharacter
            .execute(GetCharacter.RequestValues(false, id))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    characterMapper.mapOptional(
                        (response as? GetCharacter.ResponseValues)?.marvelCharacter
                    )?.let {
                        Timber.i("Character received $it")
                        handleCharacter(it)
                    }
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

    private fun handleCharacter(characters: MarvelCharacter) {
        characterState.postValue(characters)
    }

    private fun handleError(it: Throwable) {
        if (it is CompositeException) {
            errorState.postValue(it.exceptions.first())
        } else {
            errorState.postValue(it)
        }
    }
}
