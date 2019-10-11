package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.VideoView
import com.bicisos.i7.bicisos.R
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import java.util.*
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_DELAY: Long = 3000
    internal var mVideoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        normalStart()
        /*val sha1 = byteArrayOf(
            0xA5.toByte(),
            0x96.toByte(),
            0xFC.toByte(),
            0xC4.toByte(),
            0x9C.toByte(),
            0x60.toByte(),
            0xEF.toByte(),
            0x34.toByte(),
            0x54.toByte(),
            0x37.toByte(),
            0x44.toByte(),
            0xFB.toByte(),
            0x05.toByte(),
            0x24.toByte(),
            0xBA.toByte(),
            0x0B.toByte(),
            0x56.toByte(),
            0x32.toByte(),
            0x6A.toByte(),
            0x28.toByte()
        )
        Log.w("last key","keyhashGooglePlaySignIn:" + Base64.encodeToString(sha1, Base64.NO_WRAP))*/

    }

    /*fun Get_hash_key() {
        val info: PackageInfo
        try {
            info = packageManager.getPackageInfo("com.bicisos.i7.bicisos", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                //val something = String(Base64.encode(md.digest(), 0))
                //val something = String(Base64.getEncoder().encode((md.digest()))
                Log.e("hash key", something)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }*/

    private fun normalStart() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        mVideoView = findViewById(R.id.videoView) as VideoView
        val uri = "android.resource://" + packageName + "/" + R.raw.splashcopy
        if (mVideoView != null) {
            mVideoView!!.setVideoURI(Uri.parse(uri))
            mVideoView!!.setZOrderOnTop(true)
            mVideoView!!.requestFocus()
            mVideoView!!.getHolder().setSizeFromLayout();
            mVideoView!!.start()
        }
        val task = object : TimerTask() {
            override fun run() {

                val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                val sesion = prefs.getString("tutorial","null")
                if (sesion!!.equals("1")){
                    val intent = Intent(applicationContext,PrincipalActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(applicationContext,TutorialActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        val timer = Timer()
        timer.schedule(task, SPLASH_SCREEN_DELAY)

        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        ////keytool -exportcert -alias sosciclista -keystore YOUR_RELEASE_KEY_PATH | openssl sha1 -binary | openssl base64
    }
}
//
