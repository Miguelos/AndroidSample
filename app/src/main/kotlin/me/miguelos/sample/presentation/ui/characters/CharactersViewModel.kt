package me.miguelos.sample.presentation.ui.characters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.schedulers.Schedulers
import me.miguelos.sample.common.TwoWaysMapper
import me.miguelos.sample.domain.usecase.GetCharacters
import me.miguelos.sample.presentation.core.BaseViewModel
import me.miguelos.sample.presentation.core.SingleLiveEvent
import me.miguelos.sample.presentation.model.MarvelCharacter
import timber.log.Timber
import me.miguelos.sample.domain.model.MarvelCharacter as DomainMarvelCharacter


class CharactersViewModel @ViewModelInject constructor(
    private val getCharacters: GetCharacters,
    private val characterMapper: TwoWaysMapper<DomainMarvelCharacter, MarvelCharacter>
) : BaseViewModel() {

    var viewState: MutableLiveData<CharactersViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable> = SingleLiveEvent()
    var charactersState: MutableLiveData<List<MarvelCharacter>> = SingleLiveEvent()


    init {
        viewState.value = CharactersViewState(isLoading = false)
    }

    fun loadCharacters(query: String? = null, totalItemsCount: Int? = 0) {
        if (viewState.value?.isLoading == false) {
            viewState.value = viewState.value?.copy(isLoading = true)
            if (query != null) {
                viewState.value = viewState.value?.copy(
                    query = query
                )
            }
            performGetCharacterList(
                totalItemsCount = totalItemsCount,
                query = viewState.value?.query,
                ranked = viewState.value?.rankingEnabled ?: false
            )
        }
    }

    private fun performGetCharacterList(
        totalItemsCount: Int? = null,
        query: String? = null,
        ranked: Boolean = false
    ) {
        Timber.tag(javaClass.simpleName)
            .i("Getting character list (query: \"$query\" items: $totalItemsCount)")

        addDisposable(
            getCharacters.execute(
                GetCharacters.RequestValues(
                    isForceUpdate = false,
                    ranked = ranked,
                    query = query,
                    offset = totalItemsCount,
                    limit = if (query == null) {
                        PAGE_SIZE
                    } else {
                        null
                    }
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    characterMapper.mapOptional(
                        (response as? GetCharacters.ResponseValues)?.marvelCharacters
                    )?.toList()?.let {
                        Timber.i("Characters received $it")
                        handleCharacters(it)
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

    private fun handleCharacters(characters: List<MarvelCharacter>) {
        charactersState.postValue(characters)
    }

    private fun handleError(it: Throwable) {
        if (it is CompositeException) {
            errorState.postValue(it.exceptions.first())
        } else {
            errorState.postValue(it)
        }
    }

    fun enableSelection() {
        viewState.value = viewState.value?.copy(
            selectionEnabled = true
        )
    }

    fun enableRanking() {
        viewState.value = viewState.value?.copy(
            rankingEnabled = true
        )
    }

    fun isSelectionEnabled(): Boolean = viewState.value?.selectionEnabled ?: false

    fun isRankingEnabled(): Boolean = viewState.value?.rankingEnabled ?: false

    companion object {
        const val PAGE_SIZE = 10
    }
}
