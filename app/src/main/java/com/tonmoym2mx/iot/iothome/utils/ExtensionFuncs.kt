package com.tonmoym2mx.iot.iothome.utils

private const val TAG = "ExtensionFuncs"
fun Float.normalize(): String = String.format("%.0f",this)

inline fun<T> exH(func:()->T):T?{
    return try {
        func.invoke()
    }catch (e:java.lang.Exception){
        println(e.localizedMessage)
        null
    }
}
fun Float.isInteger() = this%1==0.0f

