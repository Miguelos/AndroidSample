package me.miguelos.sample.data.source.remote.api.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)

data class MarvelCharacterEntity(
    @JsonProperty(Properties.ID) var id: Long = -1L,
    @JsonProperty(Properties.NAME) var name: String = "",
    @JsonProperty(Properties.DESCRIPTION) var description: String = "",
    @JsonProperty(Properties.THUMBNAIL) var thumbnail: ImageEntity = ImageEntity(),
    @JsonProperty(Properties.URLS) var urls: List<UrlEntity> = ArrayList()
) {

    object Properties {
        const val ID = "id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val THUMBNAIL = "thumbnail"
        const val URLS = "urls"
    }
}
