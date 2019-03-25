package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.bicisos.i7.bicisos.Adapters.CustomReport
import com.bicisos.i7.bicisos.Model.Report
import com.bicisos.i7.bicisos.R

import kotlinx.android.synthetic.main.content_reportes.*

class ReportesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)
        //toolbar.title = ""
        //setSupportActionBar(toolbar)
        
        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        reportarButton.setOnClickListener {
            //validar sesion
            val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            val sesion = preferences.getString("sesion","null")
            if (sesion!!.equals("1")){

            }else{
                val intent = Intent(this,SesionActivity::class.java)
                intent.putExtra("from","Reportes")
                startActivity(intent)
            }
        }

        val reportes = ArrayList<Report>()
        reportes.add(Report(1,"url","Reporte 1"))
        reportes.add(Report(1,"url","Reporte 1"))
        reportes.add(Report(1,"url","Reporte 1"))
        reportes.add(Report(1,"url","Reporte 1"))
        reportes.add(Report(1,"url","Reporte 1"))
        reportes.add(Report(1,"url","Reporte 1"))
        reportes.add(Report(1,"url","Reporte 1"))
        reportes.add(Report(1,"url","Reporte 1"))
        val adapter = CustomReport(this,reportes)

        listaReportes.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        listaReportes.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        //regresa de sesion y checamos user
        Log.e("sesion",getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).getString("sesion","null"))
    }
}
