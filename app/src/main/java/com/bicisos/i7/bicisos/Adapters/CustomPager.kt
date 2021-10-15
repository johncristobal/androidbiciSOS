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
import com.bicisos.i7.bicisos.Api.ApiUrls
import com.bicisos.i7.bicisos.R
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.imagen_bici.view.*

class CustomPager (val context: Context, val imagenes: List<String>, val id: String) : RecyclerView.Adapter<CustomPager.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        // crear inflater desde aqui para recuperar layout custom
        val view = ViewHolder(LayoutInflater.from(context).inflate(R.layout.imagen_reporte,p0,false))
        return view
    }

    override fun getItemCount(): Int {
        return imagenes.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        //asignar elementos a viewhlder
        val urlImage = ApiUrls.urlApi+"/"+id+"/"+imagenes[p1]+".png"
        Picasso
            .get()
            .load(urlImage)
            //.centerCrop()
            .into(p0.imagenBici)

//        Glide.with(context)
//            .load(urlImage)
//            //.override(100,200)
//            .signature(ObjectKey(System.currentTimeMillis()))
//            .into(p0.imagenBici)
    }

    //clase viewholder con elementos de lista
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagenBici = view.imageViewBici
    }

}