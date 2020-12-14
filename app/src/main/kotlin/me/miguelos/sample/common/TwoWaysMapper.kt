package me.miguelos.sample.common

import io.reactivex.rxjava3.core.Observable


abstract class TwoWaysMapper<E, T> {

    abstract fun mapFrom(from: E): T

    abstract fun inverseMapFrom(from: T): E

    fun mapOptional(from: E?): T? = from?.let { mapFrom(it) }

    fun inverseMapOptional(from: T?): E? = from?.let { inverseMapOptional(it) }

    fun mapFrom(from: Collection<E>): Collection<T> =
        from.map { mapFrom(it) }

    fun mapOptional(from: Collection<E>?): Collection<T>? =
        from?.mapNotNull { mapOptional(it) }

    fun inverseMapFrom(from: Collection<T>): Collection<E> =
        from.map { inverseMapFrom(it) }

    fun inverseMapOptional(from: Collection<T>?): Collection<E>? =
        from?.mapNotNull { inverseMapOptional(it) }

    fun observable(from: E): Observable<T> =
        Observable.fromCallable { mapFrom(from) }

    fun observable(from: List<E>): Observable<List<T>> =
        Observable.fromCallable { from.map { mapFrom(it) } }
}
