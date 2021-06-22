package com.bicisos.i7.bicisos.Api

import android.util.Log
import com.bicisos.i7.bicisos.model.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import java.io.IOException

abstract class SafeRequest {
    suspend fun <T: Any> apiRequest(call: suspend () -> Response<T>) : T {
        val response = call.invoke()
        if (response.isSuccessful){
            return response.body()!!
        }else{
            val gson = Gson()
            val type = object : TypeToken<ErrorResponse>() {}.type
            val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()?.charStream(), type)
            Log.e("data error", errorResponse.toString())
            //return errorResponse!!
            throw errorResponse?.error?.let { ApiException(it) }!!
        }
    }
}

class ApiException (message: String) : IOException(message)
