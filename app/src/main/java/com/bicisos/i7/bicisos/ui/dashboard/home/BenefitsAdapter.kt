package com.bicisos.i7.bicisos.ui.dashboard.home

import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bicisos.i7.bicisos.Adapters.CustomBici
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.benefits_item.view.*

class BenefitsAdapter (
    val context: Context,
    val imagenes: TypedArray,
    val textTitles: Array<String>
) : RecyclerView.Adapter<BenefitsAdapter.ViewHolder>(){


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagenBici = view.imageViewDash
        val textTitle = view.textViewDash
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.benefits_item, parent, false)
        )
        return view
    }

    override fun getItemCount(): Int {
        return imagenes.length()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imagenBici.setImageResource(imagenes.getResourceId(position,0))
        holder.textTitle.text = textTitles[position]
    }
}