package com.tonmoym2mx.iot.iothome.di

import com.tonmoym2mx.iot.iothome.network.HomeApiHelper
import com.tonmoym2mx.iot.iothome.network.RetrofitBuilder
import com.tonmoym2mx.iot.iothome.repository.HomeIoTRepository
import org.koin.dsl.module

val appModule = module {
    factory { RetrofitBuilder.apiService }
    factory { HomeApiHelper(get()) }
    factory { HomeIoTRepository(get()) }
}