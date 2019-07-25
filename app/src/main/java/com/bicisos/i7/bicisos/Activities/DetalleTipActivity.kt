package com.bicisos.i7.bicisos.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bicisos.i7.bicisos.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detalle_tip.*

class DetalleTipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_tip)

        val tip = intent.getStringExtra("tip")
        textViewCenter.text = tip

        Glide.with(this)
            .load(intent.getIntExtra("imagen",0))
            //.override(200,400)
            .into(imagenDetalle)

        textDetalle.text = intent.getStringExtra("descrip")
    }
}
