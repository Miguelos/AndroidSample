package me.miguelos.sample.data.source.local

import io.reactivex.rxjava3.core.Single
import me.miguelos.sample.common.TwoWaysMapper
import me.miguelos.sample.data.source.local.entity.MarvelCharacterDBEntity
import me.miguelos.sample.domain.MarvelDataSource
import me.miguelos.sample.domain.model.MarvelCharacter
import me.miguelos.sample.domain.usecase.GetCharacter
import me.miguelos.sample.domain.usecase.GetCharacters
import me.miguelos.sample.domain.usecase.SaveCharacters
import javax.inject.Inject


/**
 * Concrete implementation of a data source as a db.
 */
class MarvelLocalDataSource @Inject constructor(
    val marvelCharacterDao: MarvelCharacterDao,
    private val characterMapper: TwoWaysMapper<MarvelCharacterDBEntity, MarvelCharacter>
) : MarvelDataSource {

    override fun getMarvelCharacters(
        requestValues: GetCharacters.RequestValues
    ): Single<GetCharacters.ResponseValues?> =
        Single.fromCallable { marvelCharacterDao.getMarvelCharacters() }
            .map { list ->
                val from = requestValues.offset
                val to = requestValues.offset + requestValues.limit
                GetCharacters.ResponseValues(
                    if (from in list.indices && to - 1 in list.indices) {
                        characterMapper.mapFrom(list).sortedBy { it.name }.toList().subList(
                            requestValues.offset,
                            requestValues.offset + requestValues.limit
                        )
                    } else {
                        emptyList()
                    }
                )
            }

    override fun getMarvelCharacter(
        requestValues: GetCharacter.RequestValues
    ): Single<GetCharacter.ResponseValues?> =
        Single.fromCallable { marvelCharacterDao.getMarvelCharacterById(requestValues.id) }
            .map {
                GetCharacter.ResponseValues(characterMapper.mapOptional(it))
            }

    override fun deleteAllMarvelCharacters() {
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
