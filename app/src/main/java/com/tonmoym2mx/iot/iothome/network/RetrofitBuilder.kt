package com.tonmoym2mx.iot.iothome.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {
    private const val BASE_URL = "http://tonmoyhomeapi.abc/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build() //Doesn't require the adapter
    }
    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder.connectTimeout(1, TimeUnit.MINUTES)
        okhttpClientBuilder.readTimeout(1, TimeUnit.MINUTES)
        okhttpClientBuilder.writeTimeout(1, TimeUnit.MINUTES)
        okhttpClientBuilder.addInterceptor(loggingInterceptor)
        return okhttpClientBuilder.build()
    }
    val apiService: HomeIoTApiService = getRetrofit().create(HomeIoTApiService::class.java)
}

