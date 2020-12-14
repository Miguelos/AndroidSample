package me.miguelos.sample.data.source.remote

import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.common.Mapper
import me.miguelos.sample.data.source.remote.api.MarvelService
import me.miguelos.sample.data.source.remote.api.entity.MarvelCharacterEntity
import me.miguelos.sample.data.source.remote.api.responses.ServerError
import me.miguelos.sample.domain.MarvelDataSource
import me.miguelos.sample.domain.model.MarvelCharacter
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.domain.usecase.GetCharacters
import javax.inject.Inject

/**
 * Implementation of the data source that connects to MARVEL API.
 */
class MarvelRemoteDataSource @Inject constructor(
    private val marvelService: MarvelService,
    private val characterMapper: Mapper<MarvelCharacterEntity, MarvelCharacter>
) : MarvelDataSource {

    override fun getMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?> =
        marvelService.getCharacters(requestValues.offset, requestValues.limit)
            .map { response ->
                when {
                    response.hasData -> {
                        GetCharacters.ResponseValues(
                            characterMapper.mapOptional(response.data?.results)?.toList()
                        )
                    }
                    response.hasError -> {
                        GetCharacters.ResponseValues(
                            error = ServerError(response.message)
                        )
                    }
                    else -> {
                        GetCharacters.ResponseValues(
                            error = ServerError("No data")
                        )
                    }
                }
            }

    override fun getMarvelCharacter(
        requestValues: GetCharacter.RequestValues
    ): Single<GetCharacter.ResponseValues?> =
        marvelService.getCharacter(requestValues.id)
            .map { response ->
                when {
                    response.hasData -> {
                        GetCharacter.ResponseValues(
                            characterMapper.mapOptional(response.data?.results?.get(0))
                        )
                    }
                    response.hasError -> {
                        GetCharacter.ResponseValues(
                            error = ServerError(response.message)
                        )
                    }
                    else -> {
                        GetCharacter.ResponseValues(
                            error = ServerError("No data")
                        )
                    }
                }
            }

    override fun refreshMarvelCharacters() {
        throw UnsupportedOperationException("Refresh Marvel Characters on the server is unsupported.")
    }

    override fun deleteAllMarvelCharacters() {
        throw UnsupportedOperationException("Delete Marvel Character on the server is unsupported.")
    }

    /*override fun saveMarvelCharacter(marvelCharacterEntity: MarvelCharacterEntity) {
        throw UnsupportedOperationException("Save Marvel Character on the server is unsupported.")
    }*/
}
