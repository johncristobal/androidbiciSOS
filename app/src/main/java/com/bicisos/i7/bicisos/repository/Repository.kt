package com.bicisos.i7.bicisos.repository

import android.util.Log
import com.bicisos.i7.bicisos.Model.ContrataModel
import com.bicisos.i7.bicisos.utils.Constants
import com.bicisos.i7.bicisos.utils.State
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow

class Repository {

    private val mPostsCollection = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_DATA).document(Constants.DOCUMENT_COTIZACIONES)

    suspend fun loginGtt(user: String, pass: String){
        Log.w("user: ",user)
    }

    fun addCotizacion(data: ContrataModel) = flow<State<DocumentReference>> {

        //mPostsCollection
        emit(State.loading())

        val postRef = mPostsCollection.set(data)

        // Emit success state with post reference
        emit(State.success(postRef))
    }
}