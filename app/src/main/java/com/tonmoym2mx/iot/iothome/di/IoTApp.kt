package com.tonmoym2mx.iot.iothome.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IoTApp: Application()  {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@IoTApp)
            modules(listOf(appModule))
        }
    }
}