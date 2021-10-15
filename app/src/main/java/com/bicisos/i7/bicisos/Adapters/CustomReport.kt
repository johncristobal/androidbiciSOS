package com.bicisos.i7.bicisos.Adapters

import android.content.Context
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bicisos.i7.bicisos.Api.ApiUrls
import com.bicisos.i7.bicisos.model.Report
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.model.reportes.Reporte
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.custom_reporte.view.*

class CustomReport(val context: Context, val reportes: List<Reporte>, val clickListener: (Reporte) -> Unit) : RecyclerView.Adapter<CustomReport.ViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_reporte,p0,false))

        return view
   }

    override fun getItemCount(): Int {
        return reportes.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.textTitle.text = "# Serie: "+ (reportes[p1].robery?.serie ?: "")
        p0.textDetalle.text = reportes[p1].description

        var foto = ""
        if(reportes[p1].robery?.photos!!.contains("sillin"))
            foto = "sillin.png"
        if(reportes[p1].robery?.photos!!.contains("manubrio"))
            foto = "manubrio.png"
        if(reportes[p1].robery?.photos!!.contains("lateral"))
            foto = "lateral.png"

        Glide.with(context)
                .load(ApiUrls.urlApi+"/"+reportes[p1].id+"/"+foto)
                .override(100,100)
                .into(p0.imagenDetalle)

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