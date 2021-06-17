package com.bicisos.i7.bicisos.Adapters

import android.content.Context
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bicisos.i7.bicisos.model.Report
import com.bicisos.i7.bicisos.R
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.custom_reporte.view.*

class CustomReport(val context: Context, val reportes: ArrayList<Report>,val clickListener: (Report) -> Unit) : RecyclerView.Adapter<CustomReport.ViewHolder>(){


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_reporte,p0,false))

        return view
   }

    override fun getItemCount(): Int {
        return reportes.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.textTitle.text = "# Serie: "+reportes[p1].serie
        p0.textDetalle.text = reportes[p1].description

        val idd =  reportes[p1].id
        val storage = FirebaseStorage.getInstance().getReference()
        val reportesStRef: StorageReference? = storage.child("reportes").child(idd).child("bici_0.png")
        reportesStRef!!.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it.toString())
                .override(100,100)
                .into(p0.imagenDetalle)
        }.addOnFailureListener {
            Glide.with(context)
                .load(R.drawable.loginiconuno)
                .override(100,100)
                .into(p0.imagenDetalle)
        }

        p0.layi.setOnClickListener {
            clickListener(reportes[p1])
        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val imagenDetalle = view.imageDetalleReporte
        val textTitle = view.textViewTitle
        val textDetalle = view.textViewDetalle
        val layi = view.layiItem


    }

}