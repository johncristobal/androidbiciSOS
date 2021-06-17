package com.bicisos.i7.bicisos.ui.dashboard.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bicisos.i7.bicisos.model.PolizasResponse
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.databinding.PolizaItemBinding

class PolizasAdapter(
    private val data: PolizasResponse,
    private val polizasFragment: Context
) : RecyclerView.Adapter<PolizasAdapter.ViewHolderPoliza>() {

    inner class ViewHolderPoliza(
        val recyclerBinding: PolizaItemBinding
    ) : RecyclerView.ViewHolder(recyclerBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPoliza {
        return ViewHolderPoliza(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.poliza_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderPoliza, position: Int) {
        holder.recyclerBinding.poliza = data.polizas.polizasInfo[position]

        //TODO click en todo el elemento o ver si ponemos un ojo
        holder.recyclerBinding.imageView11.setOnClickListener {
            //click solo en la imagen...pum
            //listener.onClickMovieListener(holder.recyclerviewBinding.backInsurance, movies[position])
        }

        holder.recyclerBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return data.polizas.polizasInfo.size
    }

}