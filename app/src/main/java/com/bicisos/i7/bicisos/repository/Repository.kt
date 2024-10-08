package com.bicisos.i7.bicisos.repository

import android.util.Log
import com.bicisos.i7.bicisos.Api.SafeRequest
import com.bicisos.i7.bicisos.Api.ServiceApi
import com.bicisos.i7.bicisos.model.ContrataModel
import com.bicisos.i7.bicisos.model.LoginBicis
import com.bicisos.i7.bicisos.model.RegisterBicis
import com.bicisos.i7.bicisos.model.polizas.Login
import com.bicisos.i7.bicisos.model.reportes.Reporte
import com.bicisos.i7.bicisos.utils.Constants
import com.bicisos.i7.bicisos.utils.State
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository constructor(private val api: ServiceApi) : SafeRequest() {

    private val mPostsCollection = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_DATA)//.document(Constants.DOCUMENT_COTIZACIONES)
    private val mUsersCollection = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS)//.document(Constants.DOCUMENT_COTIZACIONES)
    private val TAG = "Firestore_GTT"

    @ExperimentalCoroutinesApi
    fun loginGtt(user: String, pass: String) : Flow<State<out Map<String, Any>?>> = callbackFlow {

        trySend(State.loading()).isSuccess
        //get poliza data

        val docRef = mUsersCollection.document(user)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    trySend(State.success(document.data)).isSuccess
                } else {
                    Log.d(TAG, "No such document")
                    trySend(State.failed("No such document")).isSuccess
                    cancel("No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                trySend(State.failed(exception.localizedMessage!!)).isSuccess
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

        trySend(State.loading()).isSuccess

        db.collection(Constants.COLLECTION_DATA).add(data)
            //mPostsCollection.add(data)
            .addOnCompleteListener {
                trySend(State.success("Success")).isSuccess
            }
            .addOnFailureListener {
                trySend(State.failed(it.localizedMessage!!)).isSuccess
                cancel(it.message.toString())
            }

        awaitClose {
            cancel()
        }
    }

    suspend fun loginGeneral(phone: String) = apiRequest {
        api.loginGeneralContract(phone)
    }

    suspend fun loginBicis(body: LoginBicis) = apiRequest {
        api.loginBicis(body)
    }

    suspend fun registerBicis(body: RegisterBicis) = apiRequest {
        api.registerBicis(body)
    }

    suspend fun loginGoogle(body: RegisterBicis) = apiRequest {
        api.loginGoogle(body)
    }

    suspend fun loginFolioPoliza(body: Login) = apiRequest {
        api.loginContract("token-sos",body)
    }

    suspend fun sendDataContract(
        lateral: MultipartBody.Part,
        sillin: MultipartBody.Part,
        manubrio: MultipartBody.Part,
        pedal: MultipartBody.Part,
        pago: MultipartBody.Part,
        data: RequestBody,
        ) = apiRequest {
        api.sendEmailContract(
            "safd124214",
            data,
            lateral,
            pedal,
            sillin,
            manubrio,
            pago
        )
    }

    suspend fun getTalleres() = apiRequest {
        api.getTalleres()
    }

    suspend fun getReportes() = apiRequest {
        api.getReportes()
    }

// ============ Reporte ==========
    suspend fun reporteBici(body: Reporte) = apiRequest {
        api.reporteBici(body)
    }

    suspend fun reporteBiciRobo(
        lateral: MultipartBody.Part?,
        sillin: MultipartBody.Part?,
        manubrio: MultipartBody.Part?,
        pedal: MultipartBody.Part?,
        data: RequestBody,
    ) = apiRequest {
        api.reporteBiciRobo(
            "safd124214",
            data,
            lateral,
            pedal,
            sillin,
            manubrio,
        )
    }

}