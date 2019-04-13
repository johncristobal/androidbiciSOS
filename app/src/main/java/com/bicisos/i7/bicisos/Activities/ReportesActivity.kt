package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.bicisos.i7.bicisos.Adapters.CustomReport
import com.bicisos.i7.bicisos.Fragments.DetailReportFragment
import com.bicisos.i7.bicisos.Fragments.FinalReporteFragment
import com.bicisos.i7.bicisos.Fragments.ReportFragment
import com.bicisos.i7.bicisos.Model.Report
import com.bicisos.i7.bicisos.R
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.content_reportes.*

class ReportesActivity : AppCompatActivity(), ReportFragment.OnFragmentInteractionListener, FinalReporteFragment.OnFragmentInteractionListenerFinal, DetailReportFragment.FragmentDetalleListener {

    var context = this
    val reportFrag = ReportFragment.newInstance("","")
    val finalReportFrag = FinalReporteFragment.newInstance("","")
    var detailtFrag = DetailReportFragment.newInstance("","")

    companion object {
        val reportes = ArrayList<Report>()
    }

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

                saveReporte()
            }else{
                val intent = Intent(this,SesionActivity::class.java)
                intent.putExtra("from","Reportes")
                startActivity(intent)
            }
        }

        getDataReportes()
    }

    override fun onResume() {
        super.onResume()

        //regresa de sesion y checamos user
        Log.e("sesion",getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).getString("sesion","null"))
    }

    fun saveReporte(){

        //mostrar frame con datos de reporte
        layoutReporte.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().add(R.id.reporte,reportFrag).commit()

    }

    fun getDataReportes(){

        //var mDatabase : DatabaseReference;
        progressBar.visibility = View.VISIBLE
        val mDatabase = FirebaseDatabase.getInstance().getReference()
        val mDatabaseData = mDatabase.child("reportes");

        //weeeeeeee
        //que pex con eso del object : ????
        mDatabaseData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("cancel",p0.details)
                progressBar.visibility = View.INVISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.w("datos","exito")

                reportes.clear()
                p0.children.mapNotNullTo(reportes) {
                    it.getValue<Report>(Report::class.java)
                }

                reportes.reverse()

                val adapter = CustomReport(context,reportes) {
                    Log.w("dato", it.name)
                    layoutReporte.visibility = View.VISIBLE
                    detailtFrag = DetailReportFragment.newInstance(it.id,it.fotos)
                    supportFragmentManager.beginTransaction().add(R.id.reporte,detailtFrag).commit()

                }

                listaReportes.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                listaReportes.adapter = adapter

                progressBar.visibility = View.INVISIBLE

            }
        })
    }

    override fun onFragmentInteraction(message: String) {
        //reload data to show new report on recyclerview
        if (message.equals("listo")){
            //supportFragmentManager.beginTransaction().remove(reportFrag).commit()
            supportFragmentManager.beginTransaction().replace(R.id.reporte,finalReportFrag).commit()

        }else {
            layoutReporte.visibility = View.INVISIBLE
            supportFragmentManager.beginTransaction().remove(reportFrag).commit();
        }
    }

    //listo fragmetn listener
    override fun onFragmentInteractionFinal(message: String) {
        layoutReporte.visibility = View.INVISIBLE
        supportFragmentManager.beginTransaction().remove(reportFrag).commit();
        getDataReportes()
    }

    override fun detalleInteraction(message: String) {
        if(message.equals("")){
            layoutReporte.visibility = View.INVISIBLE
            supportFragmentManager.beginTransaction().remove(detailtFrag).commit();
        }
    }

}
