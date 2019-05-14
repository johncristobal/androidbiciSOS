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
    }
}
