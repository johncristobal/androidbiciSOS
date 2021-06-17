package com.bicisos.i7.bicisos.ui.dashboard.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bicisos.i7.bicisos.model.PolizasResponse
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class PolizasViewModel constructor(private val repository : Repository) : ViewModel() {

    val _uploadUI = MutableLiveData<Event<ArrayList<String>>>()
    val uploadUI: LiveData<Event<ArrayList<String>>>
        get() = _uploadUI

    val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _polizas = MutableLiveData<PolizasResponse>()
    val polizas: LiveData<PolizasResponse>
        get() = _polizas

    init {
        /*
         TODO show progress true
         - get data api
         - recyclerview
        */
        getPolizasData()

    }

    fun getPolizasData(){

        viewModelScope.launch {
            try {
                _progress.value = true
                val call = repository.loginGeneral("5511112222")
                _polizas.postValue(call)
                //recyclcer set data
                _progress.value = false
                //_uploadUI.value = Event(arrayListOf("dashboard",name))

            }catch (e: Exception){
                e.printStackTrace()
                _progress.value = false
                //_uploadUI.value = Event(arrayListOf("error",e.toString()))
            }
        }
    }
}