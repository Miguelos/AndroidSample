package me.miguelos.sample.data.source.remote.api.entity.mapper

import me.miguelos.sample.common.Mapper
import me.miguelos.sample.data.source.remote.api.entity.ImageEntity
import me.miguelos.sample.domain.model.Image
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ImageMapper
@Inject constructor() : Mapper<ImageEntity, Image>() {

    override fun mapFrom(from: ImageEntity) = Image(
        imageUrl = from.imageUrl
    )
}
