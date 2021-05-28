package com.bicisos.i7.bicisos.ui.contract.resume

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bicisos.i7.bicisos.Model.ContrataModel
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.Event
import com.google.gson.Gson

class ResumeViewModel constructor(
    private val repository : Repository,
    private val context: Context
    ): ViewModel() {

    var modelData : ContrataModel = ContrataModel()
    var imagesEncodedList : ArrayList<String>? = null
    var payment_photo : String? = null

    val _uploadUI = MutableLiveData<Event<String>>()
    val uploadUI: LiveData<Event<String>>
        get() = _uploadUI

    init {
        val prefs = context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE)
        prefs.getString("dataModelContract","")?.let { Log.w("dataModelContract", it) }
        modelData = Gson().fromJson(prefs.getString("dataModelContract",""), ContrataModel::class.java)

        val set = prefs.getString("photos", null)
        imagesEncodedList = set!!.split(",").toCollection(ArrayList())
        for (i in imagesEncodedList!!.size..3){
            imagesEncodedList!!.add("x")
        }

        payment_photo = prefs.getString("payment_photo", null)
    }

    fun sendData(){
        _uploadUI.value = Event("cerrar")
    }


}