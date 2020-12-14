package me.miguelos.sample.domain.common

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import me.miguelos.sample.util.TAG
import timber.log.Timber

/**
 * Use cases are the entry points to the domain layer.
 *
 * @param <Q> the request type
 * @param <P> the response type
 */
abstract class UseCase<Q : BaseUseCase.RequestValues?, in P : BaseUseCase.ResponseValues?>(
    private val executionSchedulers: ExecutionSchedulers
) : BaseUseCase {

    protected abstract fun buildUseCase(
        requestValues: Q?
    ): Observable<in P>

    protected abstract fun validate(requestValues: Q?): Completable

    fun execute(
        requestValues: Q? = null
    ): Observable<in P> =
        validate(requestValues).andThen(
            Observable.defer {
                buildUseCase(requestValues)
                    .subscribeOn(executionSchedulers.io())
                    .observeOn(executionSchedulers.ui())
            }
        )
            .doOnError {
                throw it
            }

    override fun log(log: String?) {
        Timber.i(TAG, log)
    }

    override fun logError(error: String?) {
        Timber.e(TAG, error)
    }
}
