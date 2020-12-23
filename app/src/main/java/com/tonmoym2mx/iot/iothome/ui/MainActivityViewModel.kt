package com.tonmoym2mx.iot.iothome.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.tonmoym2mx.iot.iothome.dataclass.common.Status
import com.tonmoym2mx.iot.iothome.dataclass.response.BoardStatus
import com.tonmoym2mx.iot.iothome.dataclass.response.SwitchResponse
import com.tonmoym2mx.iot.iothome.repository.HomeIoTRepository
import com.tonmoym2mx.iot.iothome.utils.exH
import com.tonmoym2mx.iot.iothome.utils.normalize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel(private val homeIoTRepository: HomeIoTRepository) : ViewModel() {
    companion object{
        private const val TAG = "MainActivityViewModel"
    }
    private var isSensorDataReading:Boolean = false
    val deviceNotConnection: MutableLiveData<Int> by lazy { MutableLiveData<Int>(View.GONE) }
    val lightStatus: MutableLiveData<SwitchResponse> by lazy { MutableLiveData<SwitchResponse>() }
    val fanStatus: MutableLiveData<SwitchResponse> by lazy { MutableLiveData<SwitchResponse>() }
    val boardStatus: MutableLiveData<BoardStatus> by lazy { MutableLiveData<BoardStatus>() }
    val isFanOnLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val temperatureLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>("--") }
    val humidityLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>("--") }


    val lightText:LiveData<String> = Transformations.map(lightStatus){
        if(it.isOn ==0){
            "LIGHT OFF"
        }else{
            "LIGHT ON"
        }
    }
    val fanText:LiveData<String> = Transformations.map(fanStatus){
        if(it.isOn ==0){
            "FAN OFF"
        }else{
            "FAN ON"
        }
    }
    val speedText:LiveData<String> = Transformations.map(fanStatus){
        if(it.isOn ==0){
            "FAN OFF"
        }else{
           val precent =  map(it.speed?:0,0,255,0,100)
            "$precent%"
        }
    }
    val regulatorEnable:LiveData<Boolean> = Transformations.map(fanStatus){
        it.isOn == 1
    }
    fun status(){

        viewModelScope.launch {
            isLoading.postValue(true)
            val res =  homeIoTRepository.status()
            if(res?.status == Status.SUCCESS){
                val data =res.data
                isFanOnLiveData.postValue(data?.isFanOn==1)
                fanStatus.postValue(SwitchResponse("Fan",data?.isFanOn,"",data?.fanSpeed))
                lightStatus.postValue(SwitchResponse("light1",data?.isLightOneOn))
                boardStatus.postValue(data)
                continueReadSensorData()
            }else{
                deviceNotConnection.postValue(View.VISIBLE)
            }
            isLoading.postValue(false)
        }
    }

    private fun continueReadSensorData(){
        if(!isSensorDataReading) {
            viewModelScope.launch {
                isSensorDataReading = true
                while (isSensorDataReading) {
                    val res = homeIoTRepository.readSensorData()
                    if (res?.status == Status.SUCCESS) {
                        val temp = exH { res.data?.temperature?.normalize() } ?: "--"
                        val hum = exH { res.data?.humidity?.normalize() } ?: "--"
                        temperatureLiveData.postValue("$temp °C")
                        humidityLiveData.postValue("$hum %")
                        delay(1000*5)
                        Log.i(TAG, "continueReadSensorData: Temperature : $temp °C Humidity: $hum %")
                    } else {
                        isSensorDataReading = false
                        Log.i(TAG, "Reading stop")
                    }
                }
            }
        }
    }
    fun light(isOn: Boolean){
        viewModelScope.launch {
            isLoading.postValue(true)
            val res = homeIoTRepository.light(isOn)
            if(res?.status == Status.SUCCESS){
                lightStatus.postValue(res.data)
                deviceNotConnection.postValue(View.GONE)
            }else{
                deviceNotConnection.postValue(View.VISIBLE)
            }
            isLoading.postValue(false)
        }
    }
    fun fan(isOn: Boolean, speed: Int){

        viewModelScope.launch {
            isFanOnLiveData.postValue(isOn)
            isLoading.postValue(true)
            val res = homeIoTRepository.fan(isOn, speed)
            if(res?.status == Status.SUCCESS){
                fanStatus.postValue(res.data)
                deviceNotConnection.postValue(View.GONE)
            }else{
                deviceNotConnection.postValue(View.VISIBLE)
            }
            isLoading.postValue(false)
        }

    }
    private fun map(x: Int, in_min: Int, in_max: Int, out_min: Int, out_max: Int): Int {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min
    }
}