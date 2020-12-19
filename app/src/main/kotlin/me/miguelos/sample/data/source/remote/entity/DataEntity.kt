package me.miguelos.sample.data.source.remote.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class DataEntity<T>(
    @JsonProperty(Properties.OFFSET) var offset: Int = 0,
    @JsonProperty(Properties.LIMIT) var limit: Int = 0,
    @JsonProperty(Properties.TOTAL) var total: Int = 0,
    @JsonProperty(Properties.COUNT) var count: Int = 0,
    @JsonProperty(Properties.RESULTS) var results: List<T> = ArrayList()
) {

    object Properties {
        const val OFFSET = "offset"
        const val LIMIT = "limit"
        const val TOTAL = "total"
        const val COUNT = "count"
        const val RESULTS = "results"
    }
}
