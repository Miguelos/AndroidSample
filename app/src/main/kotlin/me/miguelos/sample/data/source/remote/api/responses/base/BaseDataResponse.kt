package me.miguelos.sample.data.source.remote.api.responses.base

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import me.miguelos.sample.data.source.remote.api.entity.DataEntity

@JsonIgnoreProperties(ignoreUnknown = true)

abstract class BaseDataResponse<T>(
    @JsonProperty(Properties.DATA) var data: DataEntity<T>? = null
) : BaseResponse() {

    val hasData: Boolean
        @JsonIgnore get() =
            ((data != null) && ((data !is Collection<*>) || !(data as Collection<*>).isEmpty()))

    object Properties {
        const val DATA = "data"
    }
}
