package me.miguelos.sample.presentation.model.mappers

import me.miguelos.sample.common.TwoWaysMapper
import me.miguelos.sample.domain.model.Image
import me.miguelos.sample.domain.model.Url
import me.miguelos.sample.presentation.model.MarvelCharacter
import javax.inject.Inject
import javax.inject.Singleton
import me.miguelos.sample.domain.model.MarvelCharacter as DomainMarvelCharacter

@Singleton
class MarvelCharacterMapper
@Inject constructor() : TwoWaysMapper<DomainMarvelCharacter, MarvelCharacter>() {

    override fun mapFrom(from: DomainMarvelCharacter) = MarvelCharacter(
        id = from.id,
        name = from.name,
        description = from.description,
        thumbnail = from.thumbnail.imageUrl,
        urls = from.urls.map { it.url },
        availableComics = from.availableComics
    )

    override fun inverseMapFrom(from: MarvelCharacter) = DomainMarvelCharacter(
        id = from.id,
        name = from.name,
        description = from.description,
        thumbnail = Image(from.thumbnail),
        urls = from.urls.map { Url(url = it) },
        availableComics = from.availableComics
    )
}
