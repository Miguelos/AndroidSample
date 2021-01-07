package me.miguelos.sample.data.source.local

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.common.TwoWaysMapper
import me.miguelos.sample.data.source.MarvelRankingDataSource
import me.miguelos.sample.data.source.local.entity.MarvelCharacterDBEntity
import me.miguelos.sample.domain.model.MarvelCharacter
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.domain.usecase.GetCharacters
import me.miguelos.sample.domain.usecase.SaveCharacters
import javax.inject.Inject


/**
 * Concrete implementation of a data source as a db.
 */
class MarvelLocalRemoteDataSource @Inject constructor(
    private val marvelCharacterDao: MarvelCharacterDao,
    private val characterMapper: TwoWaysMapper<MarvelCharacterDBEntity, MarvelCharacter>
) : MarvelRankingDataSource {

    override fun getRankedMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?> =
        Single.fromCallable {
            marvelCharacterDao.getMarvelCharacters("${requestValues.query}%")
        }
            .map { list ->
                when {
                    requestValues.offset != null && requestValues.offset >= list.size -> {
                        GetCharacters.ResponseValues(emptyList())
                    }
                    requestValues.offset != null && requestValues.limit != null -> {
                        val from = requestValues.offset
                        val to = requestValues.offset + requestValues.limit
                        GetCharacters.ResponseValues(
                            if (from in list.indices && to - 1 in list.indices) {
                                characterMapper.mapFrom(list).toList().subList(
                                    requestValues.offset,
                                    requestValues.offset + requestValues.limit
                                )
                            } else {
                                emptyList()
                            }
                        )
                    }
                    else -> {
                        GetCharacters.ResponseValues(
                            characterMapper.mapFrom(list).toList()
                        )
                    }
                }
            }

    override fun getRankedMarvelCharacter(
        requestValues: GetCharacter.RequestValues
    ): Single<GetCharacter.ResponseValues?> =
        Maybe.fromCallable { marvelCharacterDao.getMarvelCharacterById(requestValues.id) }
            .map {
                GetCharacter.ResponseValues(characterMapper.mapOptional(it))
            }
            .toSingle()
            .onErrorResumeNext {
                Single.just(GetCharacter.ResponseValues())
            }

    override fun deleteRankedMarvelCharacters() {
        marvelCharacterDao.deleteMarvelCharacters()
    }

    override fun saveMarvelCharacters(
        requestValues: SaveCharacters.RequestValues
    ) {
        requestValues.marvelCharacters.forEach {
            marvelCharacterDao.insertMarvelCharacter(characterMapper.inverseMapFrom(it))
        }
    }
}
