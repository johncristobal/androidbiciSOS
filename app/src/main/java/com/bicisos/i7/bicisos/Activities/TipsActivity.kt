package com.bicisos.i7.bicisos.Activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.bicisos.i7.bicisos.Adapters.TipAdapter
import com.bicisos.i7.bicisos.Model.Tip
import com.bicisos.i7.bicisos.R

import kotlinx.android.synthetic.main.activity_tips.*
import kotlinx.android.synthetic.main.content_tips.*

class TipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
        //setSupportActionBar(toolbar)

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        val tips = ArrayList<Tip>()
        tips.add(Tip("Tips generales",R.drawable.bicicuatro,""))
        tips.add(Tip("Como asegurar tu bici",R.drawable.tipsdos,"Fijar la bicicleta en un soporte empotrado, nunca en soporte atornillado, ni en árboles.\n" +
                "• Utiliza dos candados como mínimo.\n" +
                "• Asegurar el cuadro y una de las dos llantas con cable de acero.\n" +
                "• NO fijar solo el sillín."))
        tips.add(Tip("Candados que no debes usar",R.drawable.tipstres,""))

        val adap = TipAdapter(tips,this){
            Log.w("tag",it)
        }
        tipsLista.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        tipsLista.adapter = adap
    }

}
