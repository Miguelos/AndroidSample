package me.miguelos.sample.common

import io.reactivex.rxjava3.core.Observable


abstract class Mapper<E, T> {

    abstract fun mapFrom(from: E): T

    fun mapOptional(from: E?): T? = from?.let { mapFrom(it) }

    fun mapFrom(from: Collection<E>): Collection<T> =
        from.map { mapFrom(it) }

    fun mapOptional(from: Collection<E>?): Collection<T>? = from?.let { mapFrom(it) }

    fun observable(from: E): Observable<T> =
        Observable.fromCallable { mapFrom(from) }

    fun observable(from: List<E>): Observable<List<T>> =
        Observable.fromCallable { from.map { mapFrom(it) } }
}
