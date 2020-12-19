package me.miguelos.sample.data.source.remote.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ComicsEntity(
    @JsonProperty(Properties.AVAILABLE) val available: Int = 0
) {

    object Properties {
        const val AVAILABLE = "available"
    }
}
