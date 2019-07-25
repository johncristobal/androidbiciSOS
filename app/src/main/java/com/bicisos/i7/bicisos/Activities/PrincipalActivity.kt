package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
//import android.support.design.widget.NavigationView
import androidx.core.view.GravityCompat
//import android.support.v7.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bicisos.i7.bicisos.Fragments.*
import com.bicisos.i7.bicisos.Fragments.alertas.*
import com.bicisos.i7.bicisos.Model.Biker
import com.bicisos.i7.bicisos.R
import com.facebook.AccessToken
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.content_principal.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.nav_header_principal.*
import kotlinx.android.synthetic.main.nav_header_principal.view.*


class PrincipalActivity : AppCompatActivity(), DetailReportFragment.FragmentDetalleListener,FinalReporteFragment.OnFragmentInteractionListenerFinal, NavigationView.OnNavigationItemSelectedListener, AlertaFragment.OnFragmentAlertasListener, AveriaFragment.OnFragmentInteractionListenerAveria, CicloviaFragment.OnFragmentInteractionListenerCiclovia, MapFragment.OnFragmentMapListener, HelpFragment.OnFragmentInteractionListenerHelp, ApoyoFragment.OnFragmentInteractionListenerApoyo , ReportFragment.OnFragmentInteractionListener, DetallesApoyoFragment.OnFragmentInteractionListenerDetalles {

    val mapFragment = MapFragment()
    var alertasFrag = AlertaFragment()
    val finalReportFrag = FinalReporteFragment.newInstance("","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        /*toolbar.title = ""
        //toolbar.setBackgroundColor(Color.TRANSPARENT)
        setSupportActionBar(toolbar)*/

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        /*val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()*/

        nav_view.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().add(R.id.container,mapFragment).commit()

        val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        val sesion = prefs.getString("sesion","null")
        val nombre = prefs.getString("nombre","null")
        if (sesion!!.equals("1")){
            //ocultar iniciar sesion
            val menu = nav_view.menu
            menu.getItem(5).title = "Cerrar sesión"
            nav_view.getHeaderView(0).nombrText.text = nombre
            val biciRes = prefs.getInt("biciRes",0)
            nav_view.getHeaderView(0).imageViewBici.setImageResource(biciRes)

        }else{
            nav_view.getHeaderView(0).imageViewBici.setImageResource(R.drawable.loginiconuno)
            nav_view.getHeaderView(0).nombrText.text = "SOS Ciclista"
            nav_view.getHeaderView(0).imageViewBici.setOnClickListener {

                val intent = Intent(this,SesionActivity::class.java)
                startActivity(intent)
            }
        }

        //click button to laucnn navigationdrawaner
        openMenu.setOnClickListener { view ->
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            //super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        //menuInflater.inflate(R.menu.principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_reportes -> {
                val intent = Intent(this,ReportesActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_robo -> {
                val intent = Intent(this,RoboBiciActivity::class.java)
                startActivity(intent)
            }
            /*R.id.nav_slideshow -> {
                //numero de serie
                val intent = Intent(this,SerieBiciActivity::class.java)
                startActivity(intent)
            }*/
            R.id.nav_manage -> {
                //tips
                val intent = Intent(this,TipsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                //contacto
                val intent = Intent(this,ContactoActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_send -> {
                //acerca de
                val intent = Intent(this,AcercaActivity::class.java)
                startActivity(intent)
            }
            /*R.id.nav_settings -> {
                //ajustes
            }*/
            R.id.nav_login -> {
                //sesion
                val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                val sesion = prefs.getString("sesion","null")
                if (sesion!!.equals("1")) {
                    //cerrar sesion auth....
                    //alerta para cerrar sesion
                    val alertbuilder = AlertDialog.Builder(this)
                    alertbuilder.setTitle("Atención")
                    alertbuilder.setMessage("¿Deseas cerrar sesión?")
                    alertbuilder.setPositiveButton("Si", DialogInterface.OnClickListener { dialogInterface, i ->
                        prefs.edit().putString("sesion","0").apply()
                        if (AccessToken.getCurrentAccessToken() != null){

                        }

                        nav_view.getHeaderView(0).nombrText.text = "SOS Ciclista"
                        nav_view.getHeaderView(0).imageViewBici.setImageResource(R.drawable.loginiconuno)
                        val menu = nav_view.menu
                        menu.getItem(5).title = "Iniciar sesión"
                    })
                    alertbuilder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->

                    })

                    val alert = alertbuilder.create()
                    alert.show()
                }
                else {
                    val intent = Intent(this, SesionActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        val sesion = prefs.getString("sesion","null")
        val nombre = prefs.getString("nombre","null")
        if (sesion!!.equals("1")){
            //ocultar iniciar sesion
            val menu = nav_view.menu
            menu.getItem(5).title = "Cerrar sesión"
            nav_view.getHeaderView(0).nombrText.text = nombre
            val biciRes = prefs.getInt("biciRes",0)
            nav_view.getHeaderView(0).imageViewBici.setImageResource(biciRes)

        }else{
            nav_view.getHeaderView(0).imageViewBici.setImageResource(R.drawable.loginiconuno)
            nav_view.getHeaderView(0).nombrText.text = "SOS Ciclista"
            nav_view.getHeaderView(0).imageViewBici.setOnClickListener {

                val intent = Intent(this,SesionActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.e("HOME Principal","stop")

        val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        val keySelf = prefs.getString("keySelf","null")
        if(keySelf != "null"){

            prefs.edit().putString("keySelf","null").apply()

            val database = FirebaseDatabase.getInstance()
            val bikersRef = database.getReference("bikers")
            bikersRef.child(keySelf!!).removeValue()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("HOME Principal","Destroy")
    }

    override fun onFragmentInteractionAlertas(message: String) {
        //alertAction.visibility = View.VISIBLE
        openMenu.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().remove(alertasFrag).commit()
    }

    override fun onFragmentAveria(message: String) {
        Log.w("vamonos","Adios fragment averia")
        //alertAction.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().remove(alertasFrag).commit()

        if(message.equals("listo")){
            supportFragmentManager.beginTransaction().add(R.id.containerAlertasFinal,finalReportFrag).commit()
        }
        /*
            en vez de lanzar que feu enviado el reporte, cargar mapa con reportes otra vbaez y no hacer el listener
            mostrar el loading
            y un mini alert con que diga alerta enviada => ok (no se si toooodo el fragment, pero podria ser, en el containerAlertas)
         */
    }

    override fun onFragmentInteractionCiclovia(message: String) {
        Log.w("vamonos","Adios fragment ciclovia")
        //alertAction.visibility = View.VISIBLE
        //openMenu.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().remove(alertasFrag).commit()

        if(message.equals("enviado")){
            supportFragmentManager.beginTransaction().add(R.id.containerAlertasFinal,finalReportFrag).commit()
        }

        /*
            en vez de lanzar que feu enviado el reporte, cargar mapa con reportes otra vbaez y no hacer el listener
            mostrar el loading
            y un mini alert con que diga alerta enviada => ok (no se si toooodo el fragment, pero podria ser, en el containerAlertas)
         */
    }

    override fun onFragmentInteractionHelp(uri: Uri) {
        Log.w("vamonos","Adios fragment help")
        //alertAction.visibility = View.VISIBLE
        openMenu.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().remove(alertasFrag).commit()

        /*
            en vez de lanzar que feu enviado el reporte, cargar mapa con reportes otra vbaez y no hacer el listener
            mostrar el loading
            y un mini alert con que diga alerta enviada => ok (no se si toooodo el fragment, pero podria ser, en el containerAlertas)
         */
    }

    override fun onFragmentInteractionApoyo(message: String) {
        Log.w("vamonos","Adios fragment apoyo")
        //alertAction.visibility = View.VISIBLE
        //openMenu.visibility = View.VISIBLE

        /*
            en vez de lanzar que feu enviado el reporte, cargar mapa con reportes otra vbaez y no hacer el listener
            mostrar el loading
            y un mini alert con que diga alerta enviada => ok (no se si toooodo el fragment, pero podria ser, en el containerAlertas)
         */
    }

    override fun onFragmentInteractionDetalles(message: String) {
        supportFragmentManager.beginTransaction().remove(alertasFrag).commit()
        if(message.equals("listo")){
            supportFragmentManager.beginTransaction().add(R.id.containerAlertasFinal,finalReportFrag).commit()
        }
    }

    override fun onFragmentInteraction(message: String) {
        //reload data to show new report on recyclerview
        //openMenu.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().remove(alertasFrag).commit()
        if(message.equals("listo")){
            supportFragmentManager.beginTransaction().add(R.id.containerAlertasFinal,finalReportFrag).commit()
        }

    }


    override fun onFragmentInteractionMap(latitud: Double, longitud: Double, sharedElement: View) {
        val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        openMenu.visibility = View.INVISIBLE

        val manager = supportFragmentManager.beginTransaction()
        manager.setCustomAnimations(R.anim.slide_in_bottom,R.anim.slide_out_up)
        manager.addSharedElement(sharedElement, "alert")
        alertasFrag = AlertaFragment.newInstance(latitud,longitud,prefs.getString("name","null")!!)
        manager.add(R.id.containerAlertas,alertasFrag).commit()
        //alertAction.visibility = View.INVISIBLE
    }

    override fun detalleInteraction(message: String) {

    }

    //listo fragmetn listener
    override fun onFragmentInteractionFinal(message: String) {
        openMenu.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().remove(finalReportFrag).commit();
    }
}
