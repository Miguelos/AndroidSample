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
class GetCharacters @Inject constructor(
    executionSchedulers: ExecutionSchedulers,
    private val marvelRepository: MarvelRepository
) : SingleUseCase<GetCharacters.RequestValues?, GetCharacters.ResponseValues?>(executionSchedulers) {

    class RequestValues(
        val isForceUpdate: Boolean = false,
        val query: String? = null,
        val offset: Int? = null,
        val limit: Int?
    ) :
        BaseUseCase.RequestValues

    class ResponseValues(
        val marvelCharacters: List<MarvelCharacter>? = null,
        val error: Throwable? = null
    ) : BaseUseCase.ResponseValues

    override fun buildUseCase(requestValues: RequestValues?): Single<ResponseValues?> =
        marvelRepository.getMarvelCharacters(requestValues!!)

    @VisibleForTesting
    public override fun validate(requestValues: RequestValues?): Completable =
        if (requestValues?.limit != null || requestValues?.query != null) {
            Completable.complete()
        } else {
            Completable.error(IllegalArgumentException("Request values must be provided."))
        }
}
