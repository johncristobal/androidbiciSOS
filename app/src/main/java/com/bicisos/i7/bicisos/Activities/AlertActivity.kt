package com.bicisos.i7.bicisos.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bicisos.i7.bicisos.Fragments.AlertBottomFragment
import com.bicisos.i7.bicisos.Fragments.alertas.ApoyoFragment
import com.bicisos.i7.bicisos.Fragments.alertas.AveriaFragment
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_alert.*

class AlertActivity : AppCompatActivity(), AveriaFragment.OnFragmentInteractionListenerAveria {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        textViewCerrarAlertas.setOnClickListener {
            finish()
        }

        imageViewBiciRobadaAlertas.setOnClickListener {
            val modalbottomSheetFragment = AlertBottomFragment()
            modalbottomSheetFragment.show(supportFragmentManager,modalbottomSheetFragment.tag)
        }

        imageViewAveriaAlertas.setOnClickListener {
            val modalbottomSheetFragment = AveriaFragment()
            modalbottomSheetFragment.show(supportFragmentManager,modalbottomSheetFragment.tag)
        }

        imageViewApoyoAlertas.setOnClickListener {
            val modalbottomSheetFragment = ApoyoFragment()
            modalbottomSheetFragment.show(supportFragmentManager,modalbottomSheetFragment.tag)
        }
    }

    override fun onFragmentAveria(message: String) {
        Log.w("tag", message)
    }
}
