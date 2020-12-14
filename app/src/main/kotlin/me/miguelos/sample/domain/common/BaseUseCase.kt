package me.miguelos.sample.domain.common

interface BaseUseCase {

    fun log(log: String?)

    fun logError(error: String?)

    /**
     * Data passed to a request.
     */
    interface RequestValues

    /**
     * Data received from a request.
     */
    interface ResponseValues
}
