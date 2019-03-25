package com.bicisos.i7.bicisos.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bicisos.i7.bicisos.Model.Report
import com.bicisos.i7.bicisos.R
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

        p0.textTitle.text = reportes[p1].textTitle
        p0.textDetalle.text = "lOREM IPSUM lOREM IPSUM lOREM IPSUM lOREM IPSUM lOREM IPSUM lOREM IPSUM lOREM IPSUM lOREM IPSUM"
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val imagenDetalle = view.imageDetalleReporte
        val textTitle = view.textViewTitle
        val textDetalle = view.textViewDetalle
    }

}