package com.bicisos.i7.bicisos.Api

import retrofit2.Response
import java.io.IOException

abstract class SafeRequest {
    suspend fun <T: Any> apiRequest(call: suspend () -> Response<T>) : T {
        val response = call.invoke()
        if (response.isSuccessful){
            return response.body()!!
        }else{
            throw ApiException(response.message())
        }
    }
}

class ApiException (message: String) : IOException(message)
