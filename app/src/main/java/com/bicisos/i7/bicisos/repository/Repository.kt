package com.bicisos.i7.bicisos.repository

import android.util.Log

class Repository {

    suspend fun loginGtt(user: String, pass: String){
        Log.w("user: ",user)
    }
}