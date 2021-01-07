package com.bicisos.i7.bicisos.ui.contract

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bicisos.i7.bicisos.repository.Repository

class ContractViewModelFactory constructor(private val repository: Repository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContractViewModel(repository) as T
    }
}