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
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.imagen_bici.view.*

class CustomPager (val context: Context, val imagenes: ArrayList<String>, val id: String) : RecyclerView.Adapter<CustomPager.ViewHolder>() {

    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("reportes").child(id!!)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        // crear inflater desde aqui para recuperar layout custom
        val view = ViewHolder(LayoutInflater.from(context).inflate(R.layout.imagen_reporte,p0,false))
        return view
    }

    override fun getItemCount(): Int {
        // :)
        return imagenes.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        //asignar elementos a viewhlder
        storageRef.child(imagenes[p1]).downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it.toString())
                //.override(100,200)
                .into(p0.imagenBici)
        }.addOnFailureListener {

        }
    }

    //clase viewholder con elementos de lista
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagenBici = view.imageViewBici
    }

}