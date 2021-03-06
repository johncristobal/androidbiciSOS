package com.bicisos.i7.bicisos.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bicisos.i7.bicisos.Api.ApiClient
import com.bicisos.i7.bicisos.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_contacto.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ContactoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()

        setContentView(R.layout.activity_contacto)

        imageViewTwitter.setOnClickListener {

            try {
                // get the Twitter app if possible
                this.packageManager.getPackageInfo("com.twitter.android", 0)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=BiciRobos_SOSmx"))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                // no Twitter app, revert to browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/BiciRobos_SOSmx?lang=es"))
                startActivity(intent)
            }

            /*val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://twitter.com/?lang=es")
            startActivity(openURL)*/
        }

        imageViewFace.setOnClickListener {

            try {
                getPackageManager().getPackageInfo("com.facebook.katana", 0)
                val versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode
                if (versionCode >= 3002850){

                    val ii = Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/groups/266612746861233/"))
                    startActivity(ii)
                }else{
                    val ii = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/266612746861233"))
                    startActivity(ii)
                }
            } catch (e: Exception) {
                val ii = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/266612746861233"))
                startActivity(ii)
            }

            /*val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.facebook.com/groups/266612746861233/")
            startActivity(openURL)
            */
        }

        buttonReportar.setOnClickListener {
            if(ReporteDesc.text.toString().equals("")){
                Toast.makeText(this,"Favor de colocar mensaje...",Toast.LENGTH_SHORT).show()
            }else{
                //show loading
                progressBarMensaje.visibility = View.VISIBLE
                buttonReportar.visibility = View.GONE

                doAsync {
                    val result = ApiClient().sendmail(ReporteDesc.text.toString())
                    uiThread {
                        progressBarMensaje.visibility = View.INVISIBLE
                        buttonReportar.visibility = View.VISIBLE
                        if (result != null) {
                            Toast.makeText(it,"Gracias, hemos recibido tus comentarios...",Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
        }
    }
}
