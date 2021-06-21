package com.bicisos.i7.bicisos.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bicisos.i7.bicisos.model.polizas.Login
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.Event
import com.bicisos.i7.bicisos.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginViewModelViewModel constructor(private val repository: Repository) : ViewModel() {

    var userName:String = ""
    var password :String = ""
    val _launch = MutableLiveData<String>()
    val launch: LiveData<String>
        get() = _launch

    val _uploadUI = MutableLiveData<Event<ArrayList<String>>>()
    val uploadUI: LiveData<Event<ArrayList<String>>>
        get() = _uploadUI

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    fun loginAction() {
        if (validarGenericForm()) {

            viewModelScope.launch {
                try {
                    _progress.value = true

                    val _pass = password.replace('/', '_')
                    val call = repository.loginFolioPoliza(Login(userName, _pass))
    //                val gson = Gson()
    //                val jsonString = gson.toJson(call)
    //                Log.w("data", jsonString)

                    _progress.value = false
                    _uploadUI.value = Event(arrayListOf("dashboard", userName))

                } catch (e: Exception) {
                    e.printStackTrace()
                    _progress.value = false
                    _uploadUI.value = Event(arrayListOf("error", e.localizedMessage))
                }
            }
        }
    }

    var phoneErrorMessage: MutableLiveData<String> = MutableLiveData()
    var passErrorMessage: MutableLiveData<String> = MutableLiveData()
    val MESSAGE = "Llena este campo correctamente."

    private fun validarGenericForm() : Boolean {
        var resp = true

        if(validFieldGeneric(this.userName)){
            phoneErrorMessage.value = MESSAGE; resp = false
        }
        else { phoneErrorMessage.value = null }

        if(validFieldGeneric(this.password)){
            passErrorMessage.value = MESSAGE; resp = false
        }
        else { passErrorMessage.value = null }

        return resp
    }

    fun validFieldGeneric(value: String?) : Boolean {
        if (value == null){
            return true
        }
        return (value.trim().isEmpty() || value.length < 3)
    }

    @ExperimentalCoroutinesApi
    private suspend fun getUserPoliza(user: String, pass: String){
        repository.loginGtt(user, pass).collect { state ->

            Log.w("state", state.toString())
            when (state) {
                is State.Loading -> {
                    Log.w("GUARDANDO", "guardando data")
                    _progress.value = true
                }

                is State.Success -> {

                    Log.w("EXITO", "todo guardado en firestore")
                    Log.w("DATA", "todo guardado en firestore ${state.data}")
                    if (pass.equals(state.data?.get("pass_temp"))) {
                        _uploadUI.value = Event(arrayListOf("dashboard", ""))
                    } else {
                        Log.w("ERROR", "pass not match")
                    }
                    _progress.value = false

                }

                is State.Failed -> {
                    _progress.value = false
                    //state.message
                    Log.w("ERROR", "tuvimos un error")
                }
            }
        }
    }

    fun launchOnboarding(){
        _launch.value = "onboarding"
    }

    fun launchContrata(){
        _launch.value = "contrata"
    }
}