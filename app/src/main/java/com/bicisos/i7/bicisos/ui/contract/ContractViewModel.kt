package com.bicisos.i7.bicisos.ui.contract

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bicisos.i7.bicisos.Model.ContrataModel
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.Event
import java.util.*

class ContractViewModel constructor(private val repository : Repository, private val context: Context) : ViewModel() {

    var modelData : ContrataModel = ContrataModel()
    val _uploadUI = MutableLiveData<Event<String>>()
    val uploadUI: LiveData<Event<String>>
        get() = _uploadUI
    val _datePickerData = MutableLiveData<String>()
    val datePickerData: MutableLiveData<String>
        get() = _datePickerData

    init {
        modelData.ejecutivo = "sos_ciclista"
    }

    fun sendDataAction(){

        //get data, validate, send, back to login
        //validate
        if(validarGenericForm()){
            //send whatsapp with data
            //save data in our database
            Log.w("ok","form ok")
        }else{
            Log.e("error","form not set")
        }
    }

    val MESSAGE = "Llena este campo correctamente"
    val MESSAGE_EMAIL = "Verifica tu correo"
    var nombreTitularErrorMessage: MutableLiveData<String> = MutableLiveData()
    var fechaNacimientoErrorMessage: MutableLiveData<String> = MutableLiveData()
    var rfcErrorMessage: MutableLiveData<String> = MutableLiveData()
    var telefonoErrorMessage: MutableLiveData<String> = MutableLiveData()
    var correoErrorMessage: MutableLiveData<String> = MutableLiveData()
    var direccionErrorMessage: MutableLiveData<String> = MutableLiveData()
    var cpErrorMessage: MutableLiveData<String> = MutableLiveData()
    var coloniaErrorMessage: MutableLiveData<String> = MutableLiveData()
    var alcaldiaErrorMessage: MutableLiveData<String> = MutableLiveData()

    private fun validarGenericForm() : Boolean {
        var resp = true

        if(validFieldGeneric(this.modelData.nombreTitular)){
            nombreTitularErrorMessage.value = MESSAGE; resp = false
        }
        else { nombreTitularErrorMessage.value = null }

        if(validFieldGeneric(this.modelData.fechaNacimiento)){
            fechaNacimientoErrorMessage.value = MESSAGE; resp = false
        } else { fechaNacimientoErrorMessage.value = null }

        if(validFieldGeneric(this.modelData.rfc)){
            rfcErrorMessage.value = MESSAGE; resp = false }
        else { rfcErrorMessage.value = null }

        if(validFieldGeneric(this.modelData.telefono)){
            telefonoErrorMessage.value = MESSAGE; resp = false }
        else { telefonoErrorMessage.value = null }

        if(validFieldEmail(this.modelData.correo)){
            correoErrorMessage.value = MESSAGE_EMAIL; resp = false }
        else { correoErrorMessage.value = null }

        if(validFieldGeneric(this.modelData.direccion)){
            direccionErrorMessage.value = MESSAGE; resp = false }
        else { direccionErrorMessage.value = null }

        if(validFieldCP(this.modelData.cp)){
            cpErrorMessage.value = MESSAGE; resp = false }
        else { cpErrorMessage.value = null }

        if(validFieldGeneric(this.modelData.colonia)){
            coloniaErrorMessage.value = MESSAGE; resp = false }
        else { coloniaErrorMessage.value = null }

        if(validFieldGeneric(this.modelData.alcaldia)){
            alcaldiaErrorMessage.value = MESSAGE; resp = false }
        else { alcaldiaErrorMessage.value = null }

        return resp
    }

    fun validFieldGeneric(value : String) : Boolean {
        return (value.trim().isEmpty() || value.length < 3)
    }

    fun validFieldCP(value : String) : Boolean {
        return (value.trim().isEmpty() || value.length < 5)
    }

    fun validFieldEmail(value : String) : Boolean {
        return (value.trim().isEmpty() || value.length < 3 || !Patterns.EMAIL_ADDRESS.matcher(value).matches())
    }

    fun showDatePicker() {
        _uploadUI.value = Event("showDatePicker")
    }

    fun setPickerData(value : String){
        _datePickerData.value = value
        modelData.fechaNacimiento = value
    }
}