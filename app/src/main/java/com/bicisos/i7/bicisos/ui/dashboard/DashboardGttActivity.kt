package com.bicisos.i7.bicisos.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_dashboard_gtt.*

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
                    findNavController(R.id.nav_host_fragment).navigate(R.id.aboutFragmentGtt)
                }
                R.id.misionFragmentGtt -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.misionFragmentGtt)
                }
                R.id.salirFragment -> {
                    finish()
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