package com.bicisos.i7.bicisos.Activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.VideoView
import com.bicisos.i7.bicisos.R
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import java.util.*

class MainActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_DELAY: Long = 3000
    internal var mVideoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        normalStart()
    }

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

                val intent = Intent(applicationContext,PrincipalActivity::class.java)
                startActivity(intent)
            }
        }
        val timer = Timer()
        timer.schedule(task, SPLASH_SCREEN_DELAY)
    }
}
