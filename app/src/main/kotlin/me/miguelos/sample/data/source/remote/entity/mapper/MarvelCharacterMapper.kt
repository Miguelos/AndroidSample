package me.miguelos.sample.data.source.remote.entity.mapper

import me.miguelos.sample.common.Mapper
import me.miguelos.sample.data.source.remote.entity.ImageEntity
import me.miguelos.sample.data.source.remote.entity.MarvelCharacterEntity
import me.miguelos.sample.data.source.remote.entity.UrlEntity
import me.miguelos.sample.domain.model.Image
import me.miguelos.sample.domain.model.MarvelCharacter
import me.miguelos.sample.domain.model.Url
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MarvelCharacterMapper
@Inject constructor(
    private val imageMapper: Mapper<ImageEntity, Image>,
    private val urlMapper: Mapper<UrlEntity, Url>
) : Mapper<MarvelCharacterEntity, MarvelCharacter>() {

    override fun mapFrom(from: MarvelCharacterEntity) = MarvelCharacter(
        id = from.id,
        name = from.name,
        description = from.description,
        thumbnail = imageMapper.mapFrom(from.thumbnail),
        urls = urlMapper.mapFrom(from.urls).toList()
    )
}
