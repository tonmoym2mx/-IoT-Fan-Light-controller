package com.tonmoym2mx.iot.iothome.dataclass

enum class StatusAlert(val isPositive:Boolean){
    SUCCESS(true),
    FAILURE(false),
    NETWORK_PROBLEM(false),
    LOCKED(false),
    SAD(false),
    PROCESSING(false),
    OTHER(false)
}