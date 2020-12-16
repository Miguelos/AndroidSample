package me.miguelos.sample.data.source.remote.entity.mapper

import me.miguelos.sample.common.Mapper
import me.miguelos.sample.data.source.remote.entity.UrlEntity
import me.miguelos.sample.domain.model.Url
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UrlMapper
@Inject constructor() : Mapper<UrlEntity, Url>() {

    override fun mapFrom(from: UrlEntity) = Url(
        type = from.type,
        url = from.url
    )
}
