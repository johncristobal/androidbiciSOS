package com.bicisos.i7.bicisos.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.Event
import com.bicisos.i7.bicisos.utils.SingleLiveEvent
import com.bicisos.i7.bicisos.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModelViewModel constructor(private val repository : Repository) : ViewModel() {

    var userName = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    val _launch = MutableLiveData<String>()
    val launch: LiveData<String>
        get() = _launch

    val _uploadUI = MutableLiveData<Event<String>>()
    val uploadUI: LiveData<Event<String>>
        get() = _uploadUI

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    @ExperimentalCoroutinesApi
    fun loginAction(){

        val name: String = if (userName.value != null) userName.value!! else ""
        Log.w("tag...",name)
        val pass: String = if (password.value != null) password.value!! else ""
        Log.w("tag...",pass)

        viewModelScope.launch {
            try {
                //Log.w("tag...",_userName.value!!)
                //Log.w("tag...",_password.value!!)
//                val data = repository.updateStatus(type)
//                if (data){
//                    updateDash(type.toString())
//                }else{
//                    Log.w("error","Error al actualizar data")
//                }
                getUserPoliza(name, pass)
                //getUserPoliza("DB7-1-89-351", "xkd928CD?")

            }catch (e: Exception){
                _progress.value = false
                e.printStackTrace()
            }
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun getUserPoliza(user: String, pass: String){
        repository.loginGtt(user, pass).collect { state ->

            Log.w("state",state.toString())
            when (state) {
                is State.Loading -> {
                    Log.w("GUARDANDO","guardando data")
                    _progress.value = true
                }

                is State.Success -> {

                    Log.w("EXITO","todo guardado en firestore")
                    Log.w("DATA","todo guardado en firestore ${state.data}")
                    if(pass.equals(state.data?.get("pass_temp"))){
                        _uploadUI.value = Event("dashboard")
                    }else{
                        Log.w("ERROR","pass not match")
                    }
                    _progress.value = false

                }

                is State.Failed -> {
                    _progress.value = false
                    //state.message
                    Log.w("ERROR","tuvimos un error")
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