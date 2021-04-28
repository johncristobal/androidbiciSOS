package com.bicisos.i7.bicisos.repository

import android.util.Log
import com.bicisos.i7.bicisos.Model.ContrataModel
import com.bicisos.i7.bicisos.utils.Constants
import com.bicisos.i7.bicisos.utils.State
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class Repository {

    private val mPostsCollection = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_DATA)//.document(Constants.DOCUMENT_COTIZACIONES)
    private val mUsersCollection = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)//.document(Constants.DOCUMENT_COTIZACIONES)
    private val TAG = "Firestore_GTT"

    @ExperimentalCoroutinesApi
    fun loginGtt(user: String, pass: String) : Flow<State<out Map<String, Any>?>> = callbackFlow {

        offer(State.loading())
        //get poliza data

        val docRef = mUsersCollection.document(user)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    offer(State.success(document.data))
                } else {
                    Log.d(TAG, "No such document")
                    offer(State.failed("No such document"))
                    cancel("No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                offer(State.failed(exception.localizedMessage!!))
                cancel(exception.message.toString())
            }

        awaitClose {
            cancel()
        }
    }

    @ExperimentalCoroutinesApi
    fun addCotizacion(data: ContrataModel) : Flow<State<String>> = callbackFlow {

        val user = Firebase.auth.currentUser
        val db = Firebase.firestore

        offer(State.loading())

        db.collection(Constants.COLLECTION_DATA).add(data)
            //mPostsCollection.add(data)
            .addOnCompleteListener {
                offer(State.success("Success"))
            }
            .addOnFailureListener {
                offer(State.failed(it.localizedMessage!!))
                cancel(it.message.toString())
            }

        awaitClose {
            cancel()
        }
    }
}