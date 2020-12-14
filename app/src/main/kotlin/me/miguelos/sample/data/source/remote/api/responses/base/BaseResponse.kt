package me.miguelos.sample.data.source.remote.api.responses.base

import android.text.TextUtils
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)

abstract class BaseResponse(
    @JsonProperty(Properties.CODE) var code: Int = 0,
    @JsonProperty(Properties.MESSAGE) var message: String? = null,
    @JsonProperty(Properties.STATUS) var status: String? = null
) {

    @get:JsonIgnore
    val hasStatus: Boolean
        get() = !TextUtils.isEmpty(status)

    @get:JsonIgnore
    val hasError: Boolean
        get() = !Status.OK.equals(status, ignoreCase = true)


    object Properties {
        const val CODE = "code"
        const val MESSAGE = "message"
        const val STATUS = "status"
    }

    object Status {
        const val OK = "ok"
    }
}
