package com.bicisos.i7.bicisos.Activities

import android.net.Uri
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.bicisos.i7.bicisos.Adapters.RiboBiciAdapter
import com.bicisos.i7.bicisos.Fragments.RoboBici.RoboDosFragment
import com.bicisos.i7.bicisos.Fragments.RoboBici.RoboTresFragment
import com.bicisos.i7.bicisos.Fragments.RoboBici.RoboUnoFragment
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.activity_robo_bici.*

class RoboBiciActivity : AppCompatActivity(), RoboUnoFragment.OnFragmentInteractionListener, RoboDosFragment.OnFragmentInteractionListener, RoboTresFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()

        setContentView(R.layout.activity_robo_bici)

        val adapter = RiboBiciAdapter(supportFragmentManager)
        pagerBici.adapter = adapter

        tab_layout.setupWithViewPager(pagerBici, true)
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
