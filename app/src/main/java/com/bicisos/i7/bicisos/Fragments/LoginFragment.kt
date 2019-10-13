package com.bicisos.i7.bicisos.Fragments



//keytool -exportcert -alias sosciclista -keystore /Users/john.cristobal/Documents/sosciclistarele | openssl sha1 -binary | openssl base64
//keytool -exportcert -list -v -alias sosciclista -keystore /Users/john.cristobal/Documents/sosciclistarele
//keytool -list -v -alias androiddebugkey -keystore ~/.android/debug.keystore

//keytool -exportcert -alias sosciclista -keystore /Users/john.cristobal/Documents/sosciclistarele | openssl sha1 -binary | openssl base64
//keytool -exportcert -alias sosciclista -keystore /Users/i7/Desktop/sosciclistarele | openssl sha1 -binary | openssl base64


import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
//import androidx.core.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bicisos.i7.bicisos.Api.ApiClient

import com.bicisos.i7.bicisos.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var callbackManager = CallbackManager.Factory.create();
    private lateinit var auth: FirebaseAuth

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private var listener : Datalistener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, mGoogleSignInOptions)
    }

    interface Datalistener {
        fun sendActivity(message: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Datalistener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        progresFace = view.findViewById(R.id.progressBarFace)
        FacebuttonTemporal = view.findViewById(R.id.Facebutton)
        login_buttonTemp = view.findViewById(R.id.login_button)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        var loginManager: LoginManager

        loginManager = LoginManager.getInstance()/*.logInWithReadPermissions(
                    activity,
                    Arrays.asList("public_profile", "email"))*/

        loginManager.registerCallback(callbackManager , object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                println("facebook loginResult : $loginResult")
                handleFacebookAccessToken(loginResult.accessToken.token);
            }

            override fun onCancel() {
                Log.w("access","not works")
                progressBarFace.visibility = View.VISIBLE
                Facebutton.visibility = View.GONE
            }

            override fun onError(exception: FacebookException) {
                println("loginResult : ${exception.localizedMessage}")
                progressBarFace.visibility = View.VISIBLE
                Facebutton.visibility = View.GONE
            }
        })

        //facebook login ==== custom
        Facebutton.setOnClickListener {

            //faceTask(activity!!).execute()
            progressBarFace.visibility = View.VISIBLE
            Facebutton.visibility = View.GONE

            if (AccessToken.getCurrentAccessToken() != null) {
                val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                editor.putString("sesion","null")
                editor.apply()
            } else {
                //login_button.performClick()
                loginManager.logInWithReadPermissions(
                    activity,
                    Arrays.asList("public_profile", "email"))
            }
        }

        //para correo y contraseñoa === deprecated...
        buttonIngresar.setOnClickListener {

            val mail = editTextCorreo.text.toString()
            val pass = editTextPass.text.toString()
            if(editTextCorreo.text.toString().equals("")){
                Toast.makeText(activity,"Favor de colocar correo...",Toast.LENGTH_SHORT).show()
            }else if(editTextPass.text.toString().equals("")){
                Toast.makeText(activity,"Favor de colocar contraseña...",Toast.LENGTH_SHORT).show()
            }else {
                progressBarIngresar.visibility = View.VISIBLE
                buttonIngresar.visibility = View.GONE

                val mAuth = FirebaseAuth.getInstance()
                mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(object : OnCompleteListener<AuthResult> {

                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success")
                            val user = mAuth.currentUser
                            val editor =
                                activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                                    .edit()
                            editor.putString("sesion", "1")
                            editor.putString("reloadData", "1")
                            editor.putString("nombre", user!!.displayName)
                            editor.apply()

                            listener!!.sendActivity("login")
                            //updateUI(user)
                        } else {
                            progressBarIngresar.visibility = View.INVISIBLE
                            buttonIngresar.visibility = View.VISIBLE

                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException())
                            try {
                                throw task.exception!!
                            } /*catch (weakPassword: FirebaseAuthWeakPasswordException) {
                                //Log.d(TAG, "onComplete: weak_password")
                                Toast.makeText(activity, "La contraseña es incorrecta....", Toast.LENGTH_SHORT).show()
                            }*/ /*catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(activity, "Validar correo....", Toast.LENGTH_SHORT).show()
                            }*/ catch (existEmail: FirebaseAuthUserCollisionException) {
                                Toast.makeText(activity, "El correo ya existe, inicia sesión...", Toast.LENGTH_SHORT)
                                    .show()
                            } catch (e: Exception) {
                                Toast.makeText(
                                    activity,
                                    "Error al crear usuario, intente más tarde...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            // if user enters wrong email.
                            // if user enters wrong password.
                            //updateUI(null)
                        }
                    }
                })
            }
        }

        //google login ==== custom
        Googlebutton.setOnClickListener {
            //faceTask(activity!!).execute()
            progressBarGoogle.visibility = View.VISIBLE
            Googlebutton.visibility = View.GONE

            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        //login_button.setReadPermissions("email","public_profile")
        /*login_button.setReadPermissions(
            Arrays.asList("public_profile", "email")
        );*/

        // If using in a fragment
        //login_button.setFragment(this)

        /*login_button.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() != null) {
                val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                editor.putString("sesion","null")
                editor.apply()
            }
        }*/

        // Callback registration
        /*login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                Log.w("access",loginResult.accessToken.token)
                handleFacebookAccessToken(loginResult.accessToken.token)
            }

            override fun onCancel() {
                // App code
                Log.w("access","not works")
            }

            override fun onError(exception: FacebookException) {
                // App code
                Log.w("access","not works eeror"+exception.localizedMessage)
            }
        })*/

        imageViewClose.setOnClickListener {
            listener!!.sendActivity("")
        }

        textViewRegister.setOnClickListener {
            listener!!.sendActivity("registrar")
        }
    }

    private fun handleFacebookAccessToken(token: String) {

        Log.d("tag", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential).addOnCompleteListener(activity!!) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("tag", "signInWithCredential:success")
                val user = auth.currentUser
                //Toast.makeText(activity,"Inicio de sesión exitoso:  "+user!!.displayName, Toast.LENGTH_SHORT).show()
                val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                editor.putString("sesion","1")
                editor.putString("reloadData","1")
                editor.putString("nombre",user!!.displayName)
                editor.apply()

                listener!!.sendActivity("login")
                //finish()
                //updateUI(user)
            } else {
                progressBarFace!!.visibility = View.INVISIBLE
                Facebutton!!.text = "Continuar con facebook"

                // If sign in fails, display a message to the user.
                Log.w("tag", "signInWithCredential:failure", task.exception)
                Toast.makeText(activity, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }
            // ...
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

                val user = auth.currentUser
                //Toast.makeText(activity,"Inicio de sesión exitoso:  "+user!!.displayName, Toast.LENGTH_SHORT).show()
                val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                editor.putString("sesion","1")
                editor.putString("reloadData","1")
                editor.putString("nombre",user!!.displayName)
                editor.apply()

                listener!!.sendActivity("login")
                //startActivity(HomeActivity.getLaunchIntent(this))
            } else {
                progressBarGoogle!!.visibility = View.INVISIBLE
                Googlebutton!!.text = "Continuar con google"
                Toast.makeText(activity!!, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            //progressBarGoogle.visibility = View.INVISIBLE
            //Googlebutton.visibility = View.VISIBLE
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                progressBarGoogle!!.visibility = View.INVISIBLE
                Googlebutton!!.text = "Continuar con google"
                Toast.makeText(activity, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }else {
            progressBarFace.visibility = View.INVISIBLE
            Facebutton.visibility = View.VISIBLE
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {

        var progresFace: ProgressBar? = null
        var FacebuttonTemporal: TextView? = null
        var login_buttonTemp: LoginButton? = null

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    class faceTask(val context: Context) : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()

            progresFace!!.visibility = View.VISIBLE
            FacebuttonTemporal!!.text = ""
        }

        override fun doInBackground(vararg params: Void?): String? {

            if (AccessToken.getCurrentAccessToken() != null) {
                val editor = context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                editor.putString("sesion","null")
                editor.apply()
                return "0"
            }else {
                return "1"
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if(result.equals("1")){
                login_buttonTemp!!.performClick()
            }
        }
    }
}
