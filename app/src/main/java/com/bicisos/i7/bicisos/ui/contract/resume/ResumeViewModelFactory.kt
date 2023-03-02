package com.bicisos.i7.bicisos.ui.contract.resume

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.bicisos.i7.bicisos.repository.Repository

@Suppress("UNCHECKED_CAST")
class ResumeViewModelFactory constructor(
    private val repository: Repository,
    private val context: Context,
    ) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return ResumeViewModel(
            repository,
            context
        ) as T
    }
}