package com.bicisos.i7.bicisos.ui.contract

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bicisos.i7.bicisos.repository.Repository

class ContractViewModelFactory constructor(
    private val repository: Repository,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContractViewModel(repository, context) as T
    }
}