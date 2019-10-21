package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.Intent
import android.net.Uri
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bicisos.i7.bicisos.Fragments.LoginFragment
import com.bicisos.i7.bicisos.Fragments.PersonalizaFragment
import com.bicisos.i7.bicisos.Fragments.RegisterFragment
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

class SesionActivity : AppCompatActivity(), LoginFragment.Datalistener, PersonalizaFragment.OnPersonalizaListener, RegisterFragment.OnFragmentInteractionListenerRegister {

    var registerFrag = RegisterFragment.newInstance("","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()

        setContentView(R.layout.activity_sesion)

        val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        val sesion = prefs.getString("sesion","null")
        if (sesion!!.equals("1")){
            supportFragmentManager.beginTransaction().add(R.id.container, PersonalizaFragment.newInstance("", ""))
                .commit()
        } else{
            supportFragmentManager.beginTransaction().add(R.id.container, LoginFragment.newInstance("", ""))
                .commit()
        }
    }

    override fun sendActivity(message: String) {
        if(message.equals("login")) {
            supportFragmentManager.beginTransaction().replace(R.id.container, PersonalizaFragment.newInstance("", ""))
                .commit()
        }
        else if(message.equals("registrar")){
            supportFragmentManager.beginTransaction().replace(R.id.container, registerFrag)
                .commit()
        }else{
            finish()
        }
    }

    override fun onFragmentInteraction(message: String) {
        finish()
    }

    override fun onFragmentInteractionRegister(uri: String) {
        if(uri.equals("login")) {
            supportFragmentManager.beginTransaction().replace(R.id.container, PersonalizaFragment.newInstance("", ""))
                .commit()
        }else{
            supportFragmentManager.beginTransaction().remove(registerFrag)
            supportFragmentManager.beginTransaction().replace(R.id.container, LoginFragment.newInstance("", ""))
                .commit()
        }
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
