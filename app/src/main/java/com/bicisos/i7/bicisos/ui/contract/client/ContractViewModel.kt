package com.bicisos.i7.bicisos.ui.contract.client

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bicisos.i7.bicisos.Model.ContrataModel
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.Event
import com.bicisos.i7.bicisos.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    @ExperimentalCoroutinesApi
    fun sendDataAction(){

        _uploadUI.value = Event("ok")
        //get data, validate, send, back to login
        //validate
//        if(validarGenericForm()){
//            modelData.fechaContratacion = Date().toString()
//            //send whatsapp with data
//            Log.w("ok","form ok")
//            var message = "Hola, quisiera iniciar mi contratacion para SEGUROSGTT\n\n"
//            message += "*Ejecutivo:*\nSOS Ciclista\n"
//            message += "*Fecha:*\n${Date()}\n\n"
//            message += "*Nombre:*\n${modelData.nombreTitular}\n"
//            if(!modelData.segundoTitular.equals("")){
//                message += "*Segundo titular:*\n${modelData.segundoTitular}\n"
//            }
//            message += "*Fecha nacimiento:*\n${modelData.fechaNacimiento}\n"
//            if(!modelData.rfc.equals("")){
//                message += "*RFC:*\n${modelData.rfc}\n"
//            }
//            message += "*Teléfono:*\n${modelData.telefono}\n"
//            message += "*Correo:*\n${modelData.correo}\n"
//            message += "*Calle y número:*\n${modelData.direccion}\n"
//            message += "*C.P.:*\n${modelData.cp}\n"
//            message += "*Colonia:*\n${modelData.colonia}\n"
//            message += "*Alcaldía o municipio:*\n${modelData.alcaldia}\n"
//
//            //save data in our database
//            viewModelScope.launch {
//                addCotizacion(message)
//            }
//
//        }else{
//            Log.e("error","form not set")
//        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun addCotizacion(message: String){
        repository.addCotizacion(modelData).collect { state ->

            Log.w("state",state.toString())
            when (state) {
                is State.Loading -> {
                    Log.w("GUARDANDO","guardando data")
                }

                is State.Success -> {
                    //state.data
                    Log.w("EXITO","todo guardado en firestore")
                    _uploadUI.value = Event(message)
                }

                is State.Failed -> {
                    //state.message
                    Log.w("ERROR","tuvimos un error")
                }
            }
        }
    }

    fun backDash(){
        _uploadUI.value = Event("cerrar")
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