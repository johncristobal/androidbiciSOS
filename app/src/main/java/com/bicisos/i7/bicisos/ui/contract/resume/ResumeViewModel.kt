package com.bicisos.i7.bicisos.ui.contract.resume

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bicisos.i7.bicisos.Model.ContrataModel
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.Event
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ResumeViewModel constructor(
    private val repository : Repository,
    private val context: Context
    ): ViewModel() {

    var modelData : ContrataModel = ContrataModel()
    var stringData : String = ""
    var imagesEncodedList : ArrayList<String>? = null
    var payment_photo : String? = null

    val _uploadUI = MutableLiveData<Event<String>>()
    val uploadUI: LiveData<Event<String>>
        get() = _uploadUI

    init {
        val prefs = context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE)
        prefs.getString("dataModelContract","")?.let {
            stringData = it
            Log.w("dataModelContract", it)
        }
        modelData = Gson().fromJson(prefs.getString("dataModelContract",""), ContrataModel::class.java)

        val set = prefs.getString("photos", null)
        imagesEncodedList = set!!.split(",").toCollection(ArrayList())
        for (i in imagesEncodedList!!.size..3){
            imagesEncodedList!!.add("x")
        }

        payment_photo = prefs.getString("payment_photo", null)
    }

    fun sendData(){

        //create form data to api request
        val fileCompleta = File(imagesEncodedList!![0])
        val filePartLateral = MultipartBody.Part.createFormData(
            "lateral",
            fileCompleta.name,
            RequestBody.create(
                MediaType.parse("image/*"),fileCompleta)
            )

        val fileManubrio = File(imagesEncodedList!![1])
        val filePartManubrio = MultipartBody.Part.createFormData(
            "manubrio",
            fileManubrio.name,
            RequestBody.create(
                MediaType.parse("image/*"),fileManubrio)
            )

        val fileSillin = File(imagesEncodedList!![2])
        val filePartSillin = MultipartBody.Part.createFormData(
            "sillin",
            fileSillin.name,
            RequestBody.create(
                MediaType.parse("image/*"),fileSillin)
        )

        val filePedal = File(imagesEncodedList!![3])
        val filePartPedal = MultipartBody.Part.createFormData(
            "pedal",
            filePedal.name,
            RequestBody.create(
                MediaType.parse("image/*"),filePedal)
        )

        val filePago = File(payment_photo!!)
        val filePartPago = MultipartBody.Part.createFormData(
            "pago",
            filePago.name,
            RequestBody.create(
                MediaType.parse("image/*"),filePago)
        )

        val bodyData = RequestBody.create(MultipartBody.FORM, stringData)

        viewModelScope.launch {
            Log.w("start","sending data...")
            try {
                repository.sendDataContract(
                    filePartLateral,
                    filePartSillin,
                    filePartManubrio,
                    filePartPedal,
                    filePartPago,
                    bodyData
                )
            }catch (e: Exception){
                e.printStackTrace()
            }

            Log.w("finish","data sent...")
            _uploadUI.value = Event("cerrar")
        }
    }
}