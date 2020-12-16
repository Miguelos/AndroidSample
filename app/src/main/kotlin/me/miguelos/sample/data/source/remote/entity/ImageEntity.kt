package me.miguelos.sample.data.source.remote.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageEntity(
    @JsonProperty(Properties.PATH) val path: String = "",
    @JsonProperty(Properties.EXTENSION) val extension: String = ""
) {

    @JsonIgnore
    val imageUrl = "$path.$extension"

    @JsonIgnore
    val isDefaultImage = path.endsWith("image_not_available", ignoreCase = true)

    object Properties {
        const val PATH = "path"
        const val EXTENSION = "extension"
    }
}
