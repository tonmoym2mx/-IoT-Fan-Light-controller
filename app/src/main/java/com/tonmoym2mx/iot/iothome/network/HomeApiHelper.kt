package com.tonmoym2mx.iot.iothome.network

class HomeApiHelper(private val homeIoTApiService: HomeIoTApiService) {
    suspend fun light(isOn:Boolean) = ApiHandler.safeApiCall { homeIoTApiService.lightOne(if(isOn) 1 else 0)  }
    suspend fun fan(isOn:Boolean,speed:Int) = ApiHandler.safeApiCall {  homeIoTApiService.fan(speed,if(isOn) 1 else 0)}
    suspend fun status() = ApiHandler.safeApiCall {  homeIoTApiService.status()}
    suspend fun readSensorData() = ApiHandler.safeApiCall {  homeIoTApiService.readSensorData()}
}