package com.tonmoym2mx.iot.iothome.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tonmoym2mx.iot.iothome.dataclass.common.Status
import com.tonmoym2mx.iot.iothome.repository.HomeIoTRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

class MainActivityViewModel(private val homeIoTRepository: HomeIoTRepository) : ViewModel() {

    fun light(isOn:Boolean) = liveData(Dispatchers.IO){

        val res = homeIoTRepository.light(isOn)
        if(res?.status == Status.SUCCESS){
            emit( res.data)
        }else{
            emit( res?.message)
        }

    }
    fun fan(isOn:Boolean,speed:Int) = liveData(Dispatchers.IO){
        val res = homeIoTRepository.fan(isOn,speed)
        if(res?.status == Status.SUCCESS){
            emit( res.data)
        }else{
            emit( res?.message)
        }
    }
}