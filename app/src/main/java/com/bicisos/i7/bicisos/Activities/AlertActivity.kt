package com.bicisos.i7.bicisos.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bicisos.i7.bicisos.Fragments.AlertBottomFragment
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_alert.*

class AlertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        textViewCerrarAlertas.setOnClickListener {
            finish()
        }

        imageViewBiciRobadaAlertas.setOnClickListener {
            //showdialog1
            val modalbottomSheetFragment = AlertBottomFragment()
            modalbottomSheetFragment.show(supportFragmentManager,modalbottomSheetFragment.tag)
        }
    }
}
