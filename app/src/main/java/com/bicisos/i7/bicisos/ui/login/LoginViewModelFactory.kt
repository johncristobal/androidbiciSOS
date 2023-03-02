package com.bicisos.i7.bicisos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.bicisos.i7.bicisos.repository.Repository

class LoginViewModelFactory constructor(private val repository: Repository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return LoginViewModelViewModel(repository) as T
    }
}
