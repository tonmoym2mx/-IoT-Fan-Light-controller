package com.tonmoym2mx.iot.iothome.network

import retrofit2.http.*

interface HomeIoTApiService {
    @POST("fan ")
    @FormUrlEncoded
    suspend fun fetchPharmacyProduct(
        @Field("speed") speed:String?=null,
        @Field("isOn") isOn:Int?=null,
    ): String
    


}