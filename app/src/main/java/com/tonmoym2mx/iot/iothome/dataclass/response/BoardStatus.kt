package com.tonmoym2mx.iot.iothome.dataclass.response


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BoardStatus(
    @SerializedName("fanSpeed")
    var fanSpeed: Int? = null,
    @SerializedName("isFanOn")
    var isFanOn: Int? = null,
    @SerializedName("isLightOneOn")
    var isLightOneOn: Int? = null,
    @SerializedName("isLightTwoOn")
    var isLightTwoOn: Int? = null,
    @SerializedName("message")
    var message: String? = null
):Serializable