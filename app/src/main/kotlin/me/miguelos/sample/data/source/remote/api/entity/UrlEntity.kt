package me.miguelos.sample.data.source.remote.api.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)

data class UrlEntity(
    @JsonProperty(Properties.TYPE) var type: String = "",
    @JsonProperty(Properties.URL) var url: String = ""
) {

    object Properties {
        const val TYPE = "type"
        const val URL = "url"
    }
}
