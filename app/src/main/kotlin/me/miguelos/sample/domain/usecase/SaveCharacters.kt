package me.miguelos.sample.domain.usecase

import androidx.annotation.VisibleForTesting
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.domain.MarvelRepository
import me.miguelos.sample.domain.common.BaseUseCase
import me.miguelos.sample.domain.common.ExecutionSchedulers
import me.miguelos.sample.domain.common.SingleUseCase
import me.miguelos.sample.domain.model.MarvelCharacter
import javax.inject.Inject

/**
 * Fetches the list of Marvel characters.
 */
class SaveCharacters @Inject constructor(
    executionSchedulers: ExecutionSchedulers,
    private val marvelRepository: MarvelRepository
) : SingleUseCase<SaveCharacters.RequestValues?, SaveCharacters.ResponseValues?>(executionSchedulers) {

    class RequestValues(
        val marvelCharacters: List<MarvelCharacter>,
        val ranking: Boolean = false
    ) : BaseUseCase.RequestValues

    class ResponseValues(
        val error: Throwable? = null
    ) : BaseUseCase.ResponseValues

    override fun buildUseCase(requestValues: RequestValues?): Single<ResponseValues?> {
        marvelRepository.saveMarvelCharacters(requestValues!!)
        return Single.just(ResponseValues())
    }

    @VisibleForTesting
    public override fun validate(requestValues: RequestValues?): Completable =
        if (requestValues?.marvelCharacters?.isEmpty() != false) {
            Completable.error(IllegalArgumentException("Request values must be provided."))
        } else {
            Completable.complete()
        }
}
