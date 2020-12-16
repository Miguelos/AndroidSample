package me.miguelos.sample.data.source

import androidx.annotation.Nullable
import androidx.annotation.VisibleForTesting
import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.data.source.local.MarvelLocalDataSource
import me.miguelos.sample.data.source.remote.MarvelRemoteDataSource
import me.miguelos.sample.domain.MarvelRepository
import me.miguelos.sample.domain.model.MarvelCharacter
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.domain.usecase.GetCharacters
import me.miguelos.sample.domain.usecase.SaveCharacters
import java.util.*
import javax.inject.Inject

/**
 * Concrete implementation to load characters from the data sources into a cache.
 *
 *
 * Use the remote data source only if the local database doesn't exist or is empty.
 */
class MarvelRepositoryImpl @Inject constructor(
    private val marvelLocalDS: MarvelLocalDataSource,
    private val marvelRemoteDS: MarvelRemoteDataSource
) : MarvelRepository {

    @VisibleForTesting
    var cachedMarvelCharacters: LinkedHashMap<Long, MarvelCharacter> = LinkedHashMap()

    @VisibleForTesting
    @Nullable
    var cacheIsDirty: Boolean = false

    /**
     * Gets Marvel characters from cache, local data source (SQLite) or remote data source,
     * in that order or preference.
     */
    override fun getMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?> {

        if (!cacheIsDirty && marvelCharactersCached(requestValues)) {
            return getCachedMarvelCharacters(requestValues)
        }

        val remoteResponse = getAndSaveRemoteMarvelCharacters(requestValues)

        return if (cacheIsDirty) {
            remoteResponse
        } else {
            Single.concat(
                getAndCacheLocalMarvelCharacters(requestValues),
                remoteResponse
            )
                .filter { response -> response?.marvelCharacters?.isNotEmpty() == true }
                .firstOrError()
        }
    }

    private fun getCachedMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?> =
        Single.just(
            GetCharacters.ResponseValues(
                cachedMarvelCharacters.values.sortedBy { it.name }.subList(
                    requestValues.offset,
                    requestValues.offset + requestValues.limit,
                )
            )
        )

    private fun marvelCharactersCached(requestValues: GetCharacters.RequestValues): Boolean =
        cachedMarvelCharacters.size > requestValues.offset * requestValues.limit

    private fun getAndCacheLocalMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?> =
        marvelLocalDS.getMarvelCharacters(requestValues)
            .doOnSuccess { values ->
                values?.marvelCharacters?.forEach {
                    cachedMarvelCharacters[it.id] = it
                }
            }

    private fun getAndSaveRemoteMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?> =
        marvelRemoteDS.getMarvelCharacters(requestValues)
            .doOnSuccess { values ->
                values?.marvelCharacters?.let { list ->
                    marvelLocalDS.saveMarvelCharacters(SaveCharacters.RequestValues(list))
                    list.forEach { cachedMarvelCharacters[it.id] = it }
                }
            }
            .doOnTerminate {
                cacheIsDirty = false
            }

    /**
     * Gets characters from local data source (sqlite) unless the table is new or empty.
     * In that case it uses the network data source. This is done to simplify the sample.
     */
    override fun getMarvelCharacter(
        requestValues: GetCharacter.RequestValues
    ): Single<GetCharacter.ResponseValues?> =
        marvelRemoteDS.getMarvelCharacter(requestValues)

    override fun refreshMarvelCharacters() {
        cacheIsDirty = true
    }

    override fun deleteAllMarvelCharacters() {
        marvelLocalDS.deleteAllMarvelCharacters()
        cachedMarvelCharacters.clear()
    }

    override fun saveMarvelCharacters(
        requestValues: SaveCharacters.RequestValues
    ) {
        requestValues.marvelCharacters.forEach { cachedMarvelCharacters[it.id] = it }
        marvelLocalDS.saveMarvelCharacters(requestValues)
    }
}
