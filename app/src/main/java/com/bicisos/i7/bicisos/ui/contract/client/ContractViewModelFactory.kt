package com.bicisos.i7.bicisos.ui.contract.client

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.bicisos.i7.bicisos.repository.Repository

class ContractViewModelFactory constructor(
    private val repository: Repository,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return ContractViewModel(
            repository,
            context
        ) as T
    }
}