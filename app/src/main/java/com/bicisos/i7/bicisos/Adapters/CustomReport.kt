package com.bicisos.i7.bicisos.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bicisos.i7.bicisos.Model.Report
import com.bicisos.i7.bicisos.R
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.custom_reporte.view.*

class CustomReport(val context: Context, val reportes: ArrayList<Report>) : RecyclerView.Adapter<CustomReport.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_reporte,p0,false))

        return view
   }

    override fun getItemCount(): Int {
        return reportes.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.textTitle.text = reportes[p1].name
        p0.textDetalle.text =  reportes[p1].description

        val idd =  reportes[p1].id
        val storage = FirebaseStorage.getInstance().getReference()
        val reportesStRef: StorageReference? = storage.child("reportes").child(idd).child("bici_0.png")
        reportesStRef!!.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it.toString())
                .override(120,120)
                .into(p0.imagenDetalle)
        }.addOnFailureListener {
            p0.imagenDetalle.setImageResource(R.drawable.loginiconuno)
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val imagenDetalle = view.imageDetalleReporte
        val textTitle = view.textViewTitle
        val textDetalle = view.textViewDetalle
    }

}