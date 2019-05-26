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
import android.view.KeyEvent
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
    var detailtFrag = DetailReportFragment.newInstance(Report())

    var flagDestroy = false

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

        serieText.setOnFocusChangeListener( object: View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {

                if(hasFocus){
                    reportarButton.visibility = View.INVISIBLE
                }else{
                    reportarButton.visibility = View.VISIBLE
                }
            }
        })

        buttonBuscar.setOnClickListener {

            if (serieText.text.equals("")){
                Log.w("no","No hya reporte")
            }else{
                layoutBuscador.clearFocus()
                //onBackPressed()

                val mDatabase = FirebaseDatabase.getInstance().getReference()
                val mDatabaseData = mDatabase.child("reportes").orderByChild("serie").equalTo(serieText.text.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        //elementtLayout.visibility = View.INVISIBLE
                        //var mfragmentTransaction = supportFragmentManager.beginTransaction()
                        //mfragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_up);

                        if(p0.exists()){
                            Log.w("data",p0.value.toString())

                            reportes.clear()
                            p0.children.mapNotNullTo(reportes) {
                                it.getValue<Report>(Report::class.java)
                            }

                            reportes.reverse()

                            val adapter = CustomReport(context,reportes) {
                                Log.w("dato", it.name)
                                listaReportes.visibility = View.GONE
                                reportarButton.visibility = View.GONE
                                detailtFrag = DetailReportFragment.newInstance(it)
                                supportFragmentManager.beginTransaction().add(R.id.reporte,detailtFrag).commit()
                            }

                            listaReportes.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                            listaReportes.adapter = adapter

                            //serieBuscadoText.setText("# Serie reportado como robado")
                            //resultFrag = ResultFragment.newInstance("# Serie no encontrado","Sin reporte de robo")
                            //mfragmentTransaction.replace(R.id.reporte,resultFrag)
                            //mfragmentTransaction.addToBackStack(null).commit()
                        }else{
                            //serieBuscadoText.setText("")
                            //resultFrag = ResultFragment.newInstance("# Serie reportado como robado",serieText.text.toString())
                            //mfragmentTransaction.replace(R.id.reporte,resultFrag)
                            //mfragmentTransaction.addToBackStack(null).commit()
                        }
                    }
                })
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

        listaReportes.visibility = View.GONE
        reportarButton.visibility = View.GONE

        //mostrar frame con datos de reporte
        var mfragmentTransaction = supportFragmentManager.beginTransaction()
        mfragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_up);
        mfragmentTransaction.add(R.id.reporte,reportFrag).commit()

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
                    listaReportes.visibility = View.GONE
                    reportarButton.visibility = View.GONE
                    detailtFrag = DetailReportFragment.newInstance(it)
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
            listaReportes.visibility = View.VISIBLE
            reportarButton.visibility = View.VISIBLE

            supportFragmentManager.beginTransaction().remove(reportFrag).commit();
        }
    }

    //listo fragmetn listener
    override fun onFragmentInteractionFinal(message: String) {
        supportFragmentManager.beginTransaction().remove(finalReportFrag).commit();
        listaReportes.visibility = View.VISIBLE
        reportarButton.visibility = View.VISIBLE
        getDataReportes()
    }

    override fun detalleInteraction(message: String) {
        listaReportes.visibility = View.VISIBLE

        if(message.equals("")){
            supportFragmentManager.beginTransaction().remove(detailtFrag).commit();
            listaReportes.visibility = View.VISIBLE
            reportarButton.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.e("HOME Reportes","Destroy")

    }

}
