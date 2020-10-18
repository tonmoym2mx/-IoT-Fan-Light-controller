package com.tonmoym2mx.iot.iothome.dataclass.common

data class ApiResponse<out T>(val status: ApiStatus, val data: T?=null, val message: String?) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(status = ApiStatus.SUCCESS, data = data, message = null)

        fun <T> error(apiStatus: ApiStatus, message: String, data: T): ApiResponse<T> =
            ApiResponse(status = apiStatus, message = message)

    }
}
enum class ApiStatus {
    SUCCESS,
    NETWORK, // IO
    TIMEOUT, // Socket
    UNKNOWN //Anything else
}