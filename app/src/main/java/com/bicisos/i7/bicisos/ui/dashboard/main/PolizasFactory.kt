package com.bicisos.i7.bicisos.ui.dashboard.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.ui.login.LoginViewModelViewModel

class PolizasFactory constructor(private val repository: Repository, private val phone: String) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return PolizasViewModel(repository, phone) as T
    }
}