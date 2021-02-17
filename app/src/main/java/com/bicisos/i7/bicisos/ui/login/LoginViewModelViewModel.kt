package com.bicisos.i7.bicisos.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.Event
import com.bicisos.i7.bicisos.utils.SingleLiveEvent
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

    fun loginAction(){

        val name: String = if (userName.value != null) userName.value!! else ""
        Log.w("tag...",name)
        val pass: String = if (password.value != null) password.value!! else ""
        Log.w("tag...",pass)

        viewModelScope.launch {
            _progress.value = true
            try {
                //Log.w("tag...",_userName.value!!)
                //Log.w("tag...",_password.value!!)
//                val data = repository.updateStatus(type)
//                if (data){
//                    updateDash(type.toString())
//                }else{
//                    Log.w("error","Error al actualizar data")
//                }
                _progress.value = false

                _uploadUI.value = Event("dashboard")

            }catch (e: Exception){
                _progress.value = false
                e.printStackTrace()
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