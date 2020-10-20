package com.tonmoym2mx.iot.iothome.repository

import com.tonmoym2mx.iot.iothome.network.HomeApiHelper

class HomeIoTRepository(private val homeApiHelper: HomeApiHelper) {
    suspend fun light(isOn:Boolean) = homeApiHelper.light(isOn)
    suspend fun fan(isOn:Boolean,speed:Int) = homeApiHelper.fan(isOn,speed)
    suspend fun status() = homeApiHelper.status()
}