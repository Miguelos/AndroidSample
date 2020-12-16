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
class GetCharacter @Inject constructor(
    executionSchedulers: ExecutionSchedulers,
    private val marvelRepositoryMarvel: MarvelRepository
) : SingleUseCase<GetCharacter.RequestValues?, GetCharacter.ResponseValues?>(executionSchedulers) {

    class RequestValues(val isForceUpdate: Boolean, val id: Long) : BaseUseCase.RequestValues

    class ResponseValues(
        val marvelCharacter: MarvelCharacter? = null,
        val error: Throwable? = null
    ) : BaseUseCase.ResponseValues

    override fun buildUseCase(requestValues: RequestValues?): Single<ResponseValues?> {
        return marvelRepositoryMarvel.getMarvelCharacter(requestValues!!)
    }

    @VisibleForTesting
    public override fun validate(requestValues: RequestValues?): Completable =
        if (requestValues?.id != null) {
            Completable.complete()
        } else {
            Completable.error(IllegalArgumentException("Request values must be provided."))
        }
}
