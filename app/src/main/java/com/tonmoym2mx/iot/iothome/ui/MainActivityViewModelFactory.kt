package com.tonmoym2mx.iot.iothome.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tonmoym2mx.iot.iothome.repository.HomeIoTRepository

class MainActivityViewModelFactory(private val homeIoTRepository: HomeIoTRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return MainActivityViewModel(homeIoTRepository) as T
    }
}