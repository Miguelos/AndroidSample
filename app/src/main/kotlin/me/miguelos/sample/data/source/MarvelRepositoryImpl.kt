package me.miguelos.sample.data.source

import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.data.source.remote.MarvelRemoteDataSource
import me.miguelos.sample.domain.MarvelRepository
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.domain.usecase.GetCharacters
import javax.inject.Inject

/**
 * Concrete implementation to load characters from the data sources into a cache.
 *
 *
 * Use the remote data source only if the local database doesn't exist or is empty.
 */
class MarvelRepositoryImpl @Inject constructor(
    val marvelRemoteDataSource: MarvelRemoteDataSource
) : MarvelRepository {

    /**
     * Gets Marvel characters from cache, local data source (SQLite) or remote data source,
     * in that order or preference.
     */
    override fun getMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?> {
        return marvelRemoteDataSource.getMarvelCharacters(requestValues)
    }

    /**
     * Gets characters from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     */
    override fun getMarvelCharacter(
        requestValues: GetCharacter.RequestValues
    ): Single<GetCharacter.ResponseValues?> {
        return marvelRemoteDataSource.getMarvelCharacter(requestValues)
    }
}
