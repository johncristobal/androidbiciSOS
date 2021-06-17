package com.bicisos.i7.bicisos.Adapters

import android.app.Activity
import android.content.Context
import android.view.View
import com.bicisos.i7.bicisos.model.Report
import com.bicisos.i7.bicisos.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.custommarker.view.*

class CustomInfoWindowGoogleMap(val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?): View? {

        return null
    }

    override fun getInfoWindow(p0: Marker?): View? {
        var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.custommarker, null)
        var mInfoWindow: Report? = p0?.tag as Report?

        mInfoView.textViewName.text = mInfoWindow?.name
        mInfoView.textViewSerie.text = mInfoWindow?.serie
        mInfoView.textViewDetalles.text = mInfoWindow?.description
        mInfoView.textViewFecha.text = mInfoWindow?.date

        return mInfoView
    }


}