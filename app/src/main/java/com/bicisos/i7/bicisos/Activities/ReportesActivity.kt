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
import android.view.View
import com.bicisos.i7.bicisos.Adapters.CustomReport
import com.bicisos.i7.bicisos.Model.Report
import com.bicisos.i7.bicisos.R
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.content_reportes.*

class ReportesActivity : AppCompatActivity() {

    var context = this
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

        val mDatabase = FirebaseDatabase.getInstance().getReference()
        val key = mDatabase.child("reportes").push().key
        mDatabase.child("reportes").child(key!!).setValue(Report(key,"url","title"))

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

                val adapter = CustomReport(context,reportes)
                listaReportes.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                listaReportes.adapter = adapter
                progressBar.visibility = View.INVISIBLE

            }
        })
    }

    class getData : AsyncTask<Void,Void,Void>(){

        override fun doInBackground(vararg p0: Void?): Void? {
            /*reportes.add(Report(1,"url","Reporte 1"))
            reportes.add(Report(1,"url","Reporte 1"))
            reportes.add(Report(1,"url","Reporte 1"))
            reportes.add(Report(1,"url","Reporte 1"))
            reportes.add(Report(1,"url","Reporte 1"))
            reportes.add(Report(1,"url","Reporte 1"))
            reportes.add(Report(1,"url","Reporte 1"))
            reportes.add(Report(1,"url","Reporte 1"))
*/
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)


        }

    }

}
