package com.bicisos.i7.bicisos.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bicisos.i7.bicisos.Fragments.MapFragment
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.content_principal.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_personaliza.*
import kotlinx.android.synthetic.main.nav_header_principal.*
import kotlinx.android.synthetic.main.nav_header_principal.view.*


class PrincipalActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

        val mapFragment = MapFragment()
        supportFragmentManager.beginTransaction().add(R.id.container,mapFragment).commit()

        val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        val sesion = prefs.getString("sesion","null")
        val nombre = prefs.getString("nombre","null")
        if (sesion!!.equals("1")){
            //ocultar iniciar sesion
            val menu = nav_view.menu
            menu.getItem(7).title = "Cerrar sesión"
            nav_view.getHeaderView(0).nombrText.text = nombre
            val biciRes = prefs.getInt("biciRes",0)
            nav_view.getHeaderView(0).imageViewBici.setImageResource(biciRes)
            nav_view.getHeaderView(0).imageViewBici.setOnClickListener {
                val intent = Intent(this,SesionActivity::class.java)
                startActivity(intent)
            }
        }else{
            nav_view.getHeaderView(0).imageViewBici.setImageResource(R.drawable.loginiconuno)
            nav_view.getHeaderView(0).nombrText.text = "SOS Ciclista"
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
            R.id.nav_slideshow -> {
                //numero de serie
                val intent = Intent(this,SerieBiciActivity::class.java)
                startActivity(intent)
            }
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
            }
            R.id.nav_settings -> {
                //ajustes
            }
            R.id.nav_login -> {
                //sesion
                val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                val sesion = prefs.getString("sesion","null")
                if (sesion!!.equals("1")) {
                    //cerrar sesion auth....
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
            menu.getItem(7).title = "Cerrar sesión"
            nav_view.getHeaderView(0).nombrText.text = nombre
            val biciRes = prefs.getInt("biciRes",0)
            nav_view.getHeaderView(0).imageViewBici.setImageResource(biciRes)
            nav_view.getHeaderView(0).imageViewBici.setOnClickListener {
                val intent = Intent(this,SesionActivity::class.java)
                startActivity(intent)
            }
        }else{
            nav_view.getHeaderView(0).imageViewBici.setImageResource(R.drawable.loginiconuno)
            nav_view.getHeaderView(0).nombrText.text = "SOS Ciclista"
        }
    }
}
