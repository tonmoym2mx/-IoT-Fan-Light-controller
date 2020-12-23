package com.tonmoym2mx.iot.iothome.network

import com.tonmoym2mx.iot.iothome.dataclass.response.BoardStatus
import com.tonmoym2mx.iot.iothome.dataclass.response.SensorData
import com.tonmoym2mx.iot.iothome.dataclass.response.SwitchResponse
import retrofit2.http.*

interface HomeIoTApiService {
    @POST("fan")
    @FormUrlEncoded
    suspend fun fan(
        @Field("speed") speed:Int?=null,
        @Field("isOn") isOn:Int?=null,
    ): SwitchResponse

    @POST("light1")
    @FormUrlEncoded
    suspend fun lightOne(
        @Field("isOn") isOn:Int?=null,
    ): SwitchResponse

    @POST("status")
    suspend fun status(
    ): BoardStatus
    @POST("readSensorData")
    suspend fun readSensorData(
    ): SensorData
    


}