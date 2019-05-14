package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import com.bicisos.i7.bicisos.Fragments.FinalReporteFragment
import com.bicisos.i7.bicisos.Fragments.ReportFragment
import com.bicisos.i7.bicisos.Fragments.ResultFragment
import com.bicisos.i7.bicisos.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_serie_bici.*
import kotlinx.android.synthetic.main.content_serie_bici.*

class SerieBiciActivity : AppCompatActivity(), ReportFragment.OnFragmentInteractionListener, FinalReporteFragment.OnFragmentInteractionListenerFinal, ResultFragment.ListenerResult {


    val reportFrag = ReportFragment.newInstance("","")
    val finalReportFrag = FinalReporteFragment.newInstance("","")
    var resultFrag = ResultFragment.newInstance("","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_bici)
        //setSupportActionBar(toolbar)

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        serieText.clearFocus()

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

        buttonBuscar.setOnClickListener {
            if (serieText.text.equals("")){
                Log.w("no","No hya reporte")
            }else{
                val mDatabase = FirebaseDatabase.getInstance().getReference()
                val mDatabaseData = mDatabase.child("reportes").orderByChild("serie").equalTo(serieText.text.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        elementtLayout.visibility = View.INVISIBLE
                        var mfragmentTransaction = supportFragmentManager.beginTransaction()
                        mfragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_up);

                        if(!p0.exists()){
                            Log.w("data",p0.value.toString())
                            //serieBuscadoText.setText("# Serie reportado como robado")
                            resultFrag = ResultFragment.newInstance("# Serie no encontrado","Sin reporte de robo")
                            mfragmentTransaction.replace(R.id.reporte,resultFrag)
                            mfragmentTransaction.addToBackStack(null).commit()
                        }else{
                            //serieBuscadoText.setText("")
                            resultFrag = ResultFragment.newInstance("# Serie reportado como robado",serieText.text.toString())
                            mfragmentTransaction.replace(R.id.reporte,resultFrag)
                            mfragmentTransaction.addToBackStack(null).commit()
                        }
                    }
                })
            }
        }
    }

    fun saveReporte(){

        //mostrar frame con datos de reporte
        //layoutReporte.visibility = View.VISIBLE
        elementtLayout.visibility = View.INVISIBLE

        var mfragmentTransaction = supportFragmentManager.beginTransaction()
        mfragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_up);
        mfragmentTransaction.add(R.id.reporte,reportFrag).commit()

    }

    override fun onFragmentInteraction(message: String) {
        //reload data to show new report on recyclerview
        if (message.equals("listo")){
            //supportFragmentManager.beginTransaction().remove(reportFrag).commit()
            supportFragmentManager.beginTransaction().replace(R.id.reporte,finalReportFrag).commit()

        }else if (message.equals("cerrar")) {
            elementtLayout.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction().remove(resultFrag).commit()

        }else
        {
            //layoutReporte.visibility = View.INVISIBLE
            elementtLayout.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction().remove(reportFrag).commit();
        }
    }

    //listo fragmetn listener
    override fun onFragmentInteractionFinal(message: String) {
        //layoutReporte.visibility = View.INVISIBLE
        supportFragmentManager.beginTransaction().remove(finalReportFrag).commit();
    }

}
