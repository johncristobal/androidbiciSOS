package com.bicisos.i7.bicisos.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_detalle_tip.*

class DetalleTipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_tip)

        val tip = intent.getStringExtra("tip")
        textViewCenter.text = tip

    }
}
