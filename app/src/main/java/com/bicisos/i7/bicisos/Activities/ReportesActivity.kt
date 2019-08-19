package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
//import android.support.design.widget.Snackbar
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
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
import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ReportesActivity : AppCompatActivity(), ReportFragment.OnFragmentInteractionListener, FinalReporteFragment.OnFragmentInteractionListenerFinal, DetailReportFragment.FragmentDetalleListener {

    var context = this
    val reportFrag = ReportFragment.newInstance(0.0,0.0,"")
    val finalReportFrag = FinalReporteFragment.newInstance("","")
    var detailtFrag = DetailReportFragment.newInstance(Report())

    var flagShow = false

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

        serieText.clearFocus()

        serieText.setOnClickListener {
            reportarButton.visibility = View.INVISIBLE
        }

        serieText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().equals("")){
                    getDataReportes()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        reportarButton.setOnClickListener {
            val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            val reportado = preferences.getString("reportado","null")
            //Si es null, no se ha reportado, reporte..
            if(reportado!!.equals("null")) {
                //validar sesion
                val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                val sesion = preferences.getString("sesion", "null")
                val reportado = preferences.getString("reportado", "null")
                if (sesion!!.equals("1")) {
                    saveReporte()
                } else {
                    val intent = Intent(this, SesionActivity::class.java)
                    intent.putExtra("from", "Reportes")
                    startActivity(intent)
                }
            }else{
                if(reportado.equals("1")){
                    //ya hay reporte,
                    val llavereporte = preferences.getString("llavereporte","null")
                    if(!llavereporte!!.equals("null")){

                        //tenemos la llave de reporte...
                        /*val manager = childFragmentManager.beginTransaction()
                            .addToBackStack("report")
                            .addSharedElement(imageViewAlerta, "report")
                            .replace(R.id.containerAlertasCustom, alertaFrag)
                        manager.commitAllowingStateLoss()*/

                        val reference = FirebaseDatabase.getInstance().getReference("reportes").child(llavereporte)
                        reference.addListenerForSingleValueEvent(object : ValueEventListener{

                            override fun onCancelled(p0: DatabaseError) {
                                Log.w("No","sin resultados")
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                preferences.edit().putString("detalleMapFragment","1").apply()
                                preferences.edit().putString("fromAlerta","1").apply()

                                val detailtFrag = DetailReportFragment.newInstance(p0.getValue(Report::class.java)!!)
                                supportFragmentManager.beginTransaction()
                                    .addToBackStack("detalles")
                                    .replace(R.id.reporte,detailtFrag)
                                    .commit()
                            }
                        })
                    }
                }
            }
        }

        /*serieText.setOnFocusChangeListener( object: View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {

                if(hasFocus){
                    reportarButton.visibility = View.INVISIBLE
                }else{
                    reportarButton.visibility = View.VISIBLE
                }
            }
        })*/

        buttonBuscar.setOnClickListener {

            hideKeyboard(this)
            reportarButton.visibility = View.VISIBLE

            if (serieText.text.toString().equals("")){
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
                            p0.children.forEach{
                                val rep = it.getValue<Report>(Report::class.java)
                                if (rep!!.tipo == 1){
                                    reportes.add(rep)
                                }
                            }

                            /*reportes.clear()
                            p0.children.mapNotNullTo(reportes) {
                                it.getValue<Report>(Report::class.java)
                            }*/

                            reportes.reverse()

                            val adapter = CustomReport(context,reportes) {
                                Log.w("dato", it.name)
                                listaReportes.visibility = View.GONE
                                reportarButton.visibility = View.GONE
                                detailtFrag = DetailReportFragment.newInstance(it)
                                supportFragmentManager.beginTransaction().add(R.id.reporte,detailtFrag).commit()
                            }

                            listaReportes.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
                            listaReportes.adapter = adapter

                            //serieBuscadoText.setText("# Serie reportado como robado")
                            //resultFrag = ResultFragment.newInstance("# Serie no encontrado","Sin reporte de robo")
                            //mfragmentTransaction.replace(R.id.reporte,resultFrag)
                            //mfragmentTransaction.addToBackStack(null).commit()
                        }else{
                            Toast.makeText(context,"# Serie no encontrado",Toast.LENGTH_SHORT).show()
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

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onResume() {
        super.onResume()

        //regresa de sesion y checamos user
        Log.e("sesion",getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).getString("sesion","null"))
    }

    fun saveReporte(){

        val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        preferences.edit().putString("fromReporte","reporteActivity").apply()

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
        val mDatabaseData = mDatabase.child("reportes")//.orderByChild("tipo").equalTo("1")
        //weeeeeeee
        //que pex con eso del object : ????
        mDatabaseData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("cancel",p0.details)
                progressBar.visibility = View.INVISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                reportes.clear()
                p0.children.forEach{
                    val rep = it.getValue<Report>(Report::class.java)
                    if (rep!!.tipo == 1){
                        reportes.add(rep)
                    }
                }

                Log.w("datos","exito")

                /*p0.children.mapNotNullTo(reportes) {
                    it.getValue<Report>(Report::class.java)
                }*/

                reportes.reverse()

                val adapter = CustomReport(context,reportes) {
                    Log.w("dato", it.name)
                    listaReportes.visibility = View.GONE
                    reportarButton.visibility = View.GONE
                    detailtFrag = DetailReportFragment.newInstance(it)
                    supportFragmentManager.beginTransaction().add(R.id.reporte,detailtFrag).commit()
                }

                listaReportes.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
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
