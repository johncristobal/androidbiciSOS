package com.bicisos.i7.bicisos.Adapters

import android.content.Context
import android.graphics.Color
//import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bicisos.i7.bicisos.Api.ApiClient
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.imagen_bici.view.*

class CustomBici (val context: Context, val imagenes: ArrayList<Int>, var index: Int) : RecyclerView.Adapter<CustomBici.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        // crear inflater desde aqui para recuperar layout custom
        val view = ViewHolder(LayoutInflater.from(context).inflate(R.layout.imagen_bici,p0,false))
        return view
    }

    override fun getItemCount(): Int {
        // :)
        return imagenes.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        //asignar elementos a viewhlder
        p0.imagenBici.setImageResource(imagenes[p1])
        p0.imagenBici.setOnClickListener {
            index=p1
            notifyDataSetChanged()

            val editor = context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE)
            editor.edit().putInt("bici",p1).apply()
            editor.edit().putInt("biciRes",imagenes[p1]).apply()

            /*doAsync {
                var result = ApiClient().callTalleres()
                uiThread {
                    Log.e("main", result!![0].coordinates)
                }
            }*/

        }

        if(index==p1){
            p0.back.setBackgroundColor(Color.parseColor("#e5e5e5"));
        }
        else
        {
            p0.back.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    //clase viewholder con elementos de lista
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagenBici = view.imageViewBici
        val back = view.backgroundView
    }

}