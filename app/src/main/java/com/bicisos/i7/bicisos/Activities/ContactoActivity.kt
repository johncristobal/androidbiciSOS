package com.bicisos.i7.bicisos.Activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_contacto.*

class ContactoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacto)

        imageViewTwitter.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://twitter.com/BiciRobos_SOSmx?lang=es")
            startActivity(openURL)
        }

        imageViewFace.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.facebook.com/groups/266612746861233/")
            startActivity(openURL)

        }
    }
}
