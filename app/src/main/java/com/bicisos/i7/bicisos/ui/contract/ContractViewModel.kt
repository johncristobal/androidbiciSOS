package com.bicisos.i7.bicisos.ui.contract

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bicisos.i7.bicisos.Model.ContrataModel
import com.bicisos.i7.bicisos.repository.Repository

class ContractViewModel constructor(private val repository : Repository) : ViewModel() {

    var model = MutableLiveData<ContrataModel>()

    var modelData = ContrataModel()

    fun sendDataAction(){

        //get data, validate, send, back to login
        val name: String = modelData.nombreTitular
        Log.w("tag...",name)


    }
}