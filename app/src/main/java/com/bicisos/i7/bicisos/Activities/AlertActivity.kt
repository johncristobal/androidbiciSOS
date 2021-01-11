package com.bicisos.i7.bicisos.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bicisos.i7.bicisos.Fragments.AlertBottomFragment
import com.bicisos.i7.bicisos.Fragments.FinalReporteFragment
import com.bicisos.i7.bicisos.Fragments.ReportFragment
import com.bicisos.i7.bicisos.Fragments.alertas.*
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.ui.GraphActivity
import com.bicisos.i7.bicisos.ui.dashboard.DashboardGttActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_alert.*
import kotlinx.android.synthetic.main.content_principal.*

class AlertActivity : AppCompatActivity(),
    AveriaFragment.OnFragmentInteractionListenerAveria,
    FinalReporteFragment.OnFragmentInteractionListenerFinal,
    ApoyoFragment.OnFragmentInteractionListenerApoyo,
    DetallesApoyoFragment.OnFragmentInteractionListenerDetalles,
    CicloviaFragment.OnFragmentInteractionListenerCiclovia,
    HelpFragment.OnFragmentInteractionListenerHelp,
    ReportFragment.OnFragmentInteractionListener{

    private lateinit var modalbottomSheetFragment: BottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        val latitud = intent.getDoubleExtra("latitud", 99.0)
        val longitud = intent.getDoubleExtra("longitud", 99.0)
        val name = intent.getStringExtra("name")

        textViewCerrarAlertas.setOnClickListener {
            finish()
        }

        imageViewBiciRobadaAlertas.setOnClickListener {
            modalbottomSheetFragment = ReportFragment.newInstance(latitud,longitud,name!!)
            modalbottomSheetFragment.show(supportFragmentManager,modalbottomSheetFragment.tag)
        }

        imageViewAveriaAlertas.setOnClickListener {
            //averiaFrag = AveriaFragment.newInstance(latitud!!,longitud!!,name!!)
            modalbottomSheetFragment = AveriaFragment.newInstance(latitud,longitud,name!!)
            modalbottomSheetFragment.show(supportFragmentManager,modalbottomSheetFragment.tag)
        }

        imageViewApoyoAlertas.setOnClickListener {
            modalbottomSheetFragment = ApoyoFragment.newInstance(latitud,longitud,name!!)
            modalbottomSheetFragment.show(supportFragmentManager,modalbottomSheetFragment.tag)
        }

        imageViewHelpAlertas.setOnClickListener {
            modalbottomSheetFragment = HelpFragment.newInstance(latitud,longitud,name!!)
            modalbottomSheetFragment.show(supportFragmentManager,modalbottomSheetFragment.tag)
        }

        imageViewCicloviaAlertas.setOnClickListener {
            modalbottomSheetFragment = CicloviaFragment.newInstance(latitud,longitud,name!!)
            modalbottomSheetFragment.show(supportFragmentManager,modalbottomSheetFragment.tag)
        }

        imageViewSegurosBici.setOnClickListener {
            //startActivity(Intent(this, GraphActivity::class.java))
            startActivity(Intent(this, DashboardGttActivity::class.java))
        }

    }

    override fun onFragmentAveria(message: String) {
        Log.w("tag", message)
    }

    override fun onFragmentInteractionFinal(message: String) {
        modalbottomSheetFragment.dismiss()
    }

    override fun onFragmentInteractionApoyo(message: String) {
        Log.w("tag", message)
    }

    override fun onFragmentInteractionDetalles(message: String) {
        Log.w("tag", message)
    }

    override fun onFragmentInteractionCiclovia(message: String) {
        Log.w("tag", message)
    }

    override fun onFragmentInteractionHelp(uri: Uri) {
        Log.w("tag", uri.path!!)
    }

    override fun onFragmentInteraction(message: String) {
        Log.w("tag", message)
    }
}
