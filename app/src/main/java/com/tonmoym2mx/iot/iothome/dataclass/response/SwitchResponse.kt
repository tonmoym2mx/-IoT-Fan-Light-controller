package com.tonmoym2mx.iot.iothome.dataclass.response


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SwitchResponse(
    @SerializedName("Device")
    var device: String? = null,
    @SerializedName("isOn")
    var isOn: Int? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("speed")
    var speed: Int? = null
):Serializable