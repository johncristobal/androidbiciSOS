package com.bicisos.i7.bicisos.ui.contract.resume

import android.content.Context
import androidx.lifecycle.ViewModel
import com.bicisos.i7.bicisos.Model.ContrataModel
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.repository.Repository
import com.google.gson.Gson

class ResumeViewModel constructor(
    private val repository : Repository,
    private val context: Context
    ): ViewModel() {

    var modelData : ContrataModel = ContrataModel()

    init {
        val prefs = context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE)
        modelData = Gson().fromJson(prefs.getString("dataModelContract",""), ContrataModel::class.java)



    }

}