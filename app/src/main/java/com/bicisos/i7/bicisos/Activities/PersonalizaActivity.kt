package com.bicisos.i7.bicisos.Activities

import android.os.Bundle
import android.view.Window
//import android.support.design.widget.Snackbar
import androidx.appcompat.app.AppCompatActivity
//import android.support.v7.widget.LinearLayoutManager
import com.bicisos.i7.bicisos.Adapters.CustomBici
import com.bicisos.i7.bicisos.R

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*

class PersonalizaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()

        setContentView(R.layout.activity_login)
        //setSupportActionBar(toolbar)

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        /*listaimagenes.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        val imagenes = ArrayList<Int>()
        imagenes.add(R.mipmap.bicia)
        imagenes.add(R.mipmap.bicib)
        imagenes.add(R.mipmap.bicic)
        imagenes.add(R.mipmap.bicid)

        val adapter = CustomBici(this,imagenes)

        listaimagenes.adapter = adapter*/
    }
}
