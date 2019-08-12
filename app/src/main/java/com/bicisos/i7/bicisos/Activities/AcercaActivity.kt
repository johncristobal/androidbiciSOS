package com.bicisos.i7.bicisos.Activities

//import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_acerca.*

class AcercaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acerca)

        btnTutorial.setOnClickListener {
            startActivity(Intent(this,TutorialActivity::class.java))
        }
    }
}
