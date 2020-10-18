package com.tonmoym2mx.iot.iothome.network

import android.util.Log
import com.tonmoym2mx.iot.iothome.dataclass.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

object ApiHandler{

    suspend inline fun <T> safeApiCall(crossinline responseFunction: suspend () -> T): Resource<T>? {
        return try{
            val response = withContext(Dispatchers.IO){ responseFunction.invoke() }
            Resource.success(response)
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                e.printStackTrace()
                Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
                when(e){
                    is HttpException -> {
                        val body = e.response()?.errorBody()

                        val messsageString  =  try {
                            val jsonObject = JSONObject(body?.string()?:"")
                            when {
                                jsonObject.has("message") -> jsonObject.getString("message")
                                jsonObject.has("error") -> jsonObject.getString("error")
                                else -> "Something wrong happened"
                            }
                        } catch (e: Exception) {
                            "Something wrong happened"
                        }
                        Resource.error(null,messsageString)
                    }
                    is IllegalArgumentException->{
                        Resource.error(null,e.message.toString())
                    }
                    else -> Resource.error(null,e.message.toString())
                }
            }

        }
    }


}
