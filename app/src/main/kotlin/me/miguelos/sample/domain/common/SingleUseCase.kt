package me.miguelos.sample.domain.common

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.util.TAG
import timber.log.Timber


abstract class SingleUseCase<Q : BaseUseCase.RequestValues?, in P : BaseUseCase.ResponseValues?>
constructor(private val executionSchedulers: ExecutionSchedulers) : BaseUseCase {

    protected abstract fun buildUseCase(requestValues: Q?): Single<in P>

    protected abstract fun validate(requestValues: Q?): Completable

    fun execute(requestValues: Q? = null): Single<in P> =
        validate(requestValues)
            .andThen(
                Single.defer {
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
