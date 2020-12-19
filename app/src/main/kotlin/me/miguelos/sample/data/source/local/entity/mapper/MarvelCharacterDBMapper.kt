package me.miguelos.sample.data.source.local.entity.mapper

import me.miguelos.sample.common.TwoWaysMapper
import me.miguelos.sample.data.source.local.entity.MarvelCharacterDBEntity
import me.miguelos.sample.domain.model.Image
import me.miguelos.sample.domain.model.MarvelCharacter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MarvelCharacterDBMapper
@Inject constructor() : TwoWaysMapper<MarvelCharacterDBEntity, MarvelCharacter>() {

    override fun mapFrom(from: MarvelCharacterDBEntity) = MarvelCharacter(
        id = from.id,
        name = from.name,
        description = from.description,
        thumbnail = Image(imageUrl = from.thumbnail),
        availableComics = from.comics
    )

    override fun inverseMapFrom(from: MarvelCharacter) = MarvelCharacterDBEntity(
        id = from.id,
        name = from.name,
        description = from.description,
        thumbnail = from.thumbnail.imageUrl,
        comics = from.availableComics
    )
}
