package com.bicisos.i7.bicisos.Adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bicisos.i7.bicisos.Activities.DetalleTipActivity
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.imagen_tip.view.*

class TipAdapter (val tips: ArrayList<String>, val context: Activity,val clickListener: (String) -> Unit) : RecyclerView.Adapter<TipAdapter.ViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val inflater = ViewHolder(LayoutInflater.from(context).inflate(R.layout.imagen_tip,p0,false))
        return inflater
    }

    override fun getItemCount(): Int {
        return tips.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.text.text = tips[p1]
        p0.text.setOnClickListener {
            val inte = Intent(context, DetalleTipActivity::class.java)
            inte.putExtra("tip",tips[p1])
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptions.makeSceneTransitionAnimation(context, p0.text, "robot")
                // Apply activity transition
                context.startActivity(inte,options.toBundle())
            } else {
                // Swap without transition
                context.startActivity(inte)
            }

        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val text = view.textViewTip
    }

}