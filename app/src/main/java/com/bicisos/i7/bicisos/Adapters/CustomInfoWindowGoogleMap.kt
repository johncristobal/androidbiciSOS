package com.bicisos.i7.bicisos.Adapters

import android.app.Activity
import android.content.Context
import android.view.View
import com.bicisos.i7.bicisos.model.Report
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.model.reportes.Reporte
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.custommarker.view.*

class CustomInfoWindowGoogleMap(val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?): View? {

        return null
    }

    override fun getInfoWindow(p0: Marker?): View? {
        val mInfoView = (context as Activity).layoutInflater.inflate(R.layout.custommarker, null)
        val mInfoWindow: Reporte? = p0?.tag as Reporte?

        var title = "Atención..."
        when (mInfoWindow!!.typeReport){
            "1" -> title = "Bici robada"
            "2" -> title = "Bici averiada"
            "3" -> title = "Ojo con ciclovía"
            "4" -> title = "Necesito apoyo"
            "5" -> title = "EMERGENCIA"
        }

        mInfoView.textViewName.text = title //mInfoWindow?.name
        mInfoView.textViewSerie.text = ""
        mInfoView.textViewDetalles.text = mInfoWindow.description
        //mInfoView.textViewFecha.text = mInfoWindow?.

        return mInfoView
    }


}