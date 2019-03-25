package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bicisos.i7.bicisos.Fragments.LoginFragment
import com.bicisos.i7.bicisos.Fragments.PersonalizaFragment
import com.bicisos.i7.bicisos.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sesion.*
import com.facebook.AccessToken

class SesionActivity : AppCompatActivity(), LoginFragment.Datalistener, PersonalizaFragment.OnPersonalizaListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sesion)

        supportFragmentManager.beginTransaction().add(R.id.container, PersonalizaFragment.newInstance("","")).commit()
    }

    override fun sendActivity(message: String) {
        supportFragmentManager.beginTransaction().replace(R.id.container, PersonalizaFragment.newInstance("","")).commit()
    }

    override fun onFragmentInteraction(message: String) {
        finish()
    }

    override fun onStop() {
        super.onStop()

        if (AccessToken.getCurrentAccessToken() != null) {
            val editor = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
            editor.putString("sesion","1")
            editor.apply()
        }
    }
}
