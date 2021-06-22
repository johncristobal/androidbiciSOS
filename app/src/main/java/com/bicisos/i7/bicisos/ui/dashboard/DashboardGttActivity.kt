package com.bicisos.i7.bicisos.ui.dashboard

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bicisos.i7.bicisos.Fragments.MapFragment
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.ui.contract.ContractActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard_gtt.*
import kotlinx.android.synthetic.main.activity_dashboard_gtt.drawer_layout
import kotlinx.android.synthetic.main.activity_dashboard_gtt.nav_view
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.nav_header_principal.view.*

class DashboardGttActivity : AppCompatActivity() {

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_gtt)
        setSupportActionBar(my_awesome_toolbar)
        setupDrawerLayout()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout)
    }

    private fun setupDrawerLayout() {
        nav_view.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)

        nav_view.setNavigationItemSelectedListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            when(it.itemId){
                R.id.contactFragmentGtt -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.contactFragmentGtt)
                }
                R.id.aboutFragmentGtt -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragmentGtt)
                }
                R.id.misionFragmentGtt -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.misionFragmentGtt)
                }
                R.id.contrata_gtt -> {
                    startActivity(Intent(this, ContractActivity::class.java))
                }
                R.id.salirFragment -> {
                    val prefs = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                    val alertbuilder = AlertDialog.Builder(this)
                    alertbuilder.setTitle("Atención")
                    alertbuilder.setMessage("¿Deseas cerrar sesión de GTT Seguros?")
                    alertbuilder.setPositiveButton("Si") { _, i ->

                        prefs.edit().putString("gttseguros", "0").apply()
                        finish()
                    }

                    alertbuilder.setNegativeButton("No") { _, i ->

                    }

                    val alert = alertbuilder.create()
                    alert.show()
                }
                else -> {

                }
            }

            true

        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}