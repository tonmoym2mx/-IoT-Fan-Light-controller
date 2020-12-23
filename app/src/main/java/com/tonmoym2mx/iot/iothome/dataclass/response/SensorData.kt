package com.tonmoym2mx.iot.iothome.dataclass.response


import com.google.gson.annotations.SerializedName

data class SensorData(
    @SerializedName("humidity")
    var humidity: Float? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("temperature")
    var temperature: Float? = null
)