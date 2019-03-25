package com.bicisos.i7.bicisos.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.bicisos.i7.bicisos.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sesion.*
import kotlinx.android.synthetic.main.fragment_login.*

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

    private var listener : Datalistener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        login_button.setReadPermissions("email","public_profile")
        // If using in a fragment
        login_button.setFragment(this)

        login_button.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() != null) {
                val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                editor.putString("sesion","null")
                editor.apply()
            }
        }

        // Callback registration
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
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
        })

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.w("access",loginResult.accessToken.token)
                    handleFacebookAccessToken(loginResult.accessToken.token)
                }

                override fun onCancel() {
                    // App code
                    Log.w("access","not works")
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    Log.w("access","not works eeror 2"+exception.localizedMessage)
                }
            }
        )
    }

    private fun handleFacebookAccessToken(token: String) {
        Log.d("tag", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential).addOnCompleteListener(activity!!) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("tag", "signInWithCredential:success")
                val user = auth.currentUser
                Toast.makeText(activity,"logrado "+user.toString(), Toast.LENGTH_SHORT).show()
                val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                editor.putString("sesion","1")
                editor.apply()

                listener!!.sendActivity("")
                //finish()
                //updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("tag", "signInWithCredential:failure", task.exception)
                Toast.makeText(activity, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
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
}
