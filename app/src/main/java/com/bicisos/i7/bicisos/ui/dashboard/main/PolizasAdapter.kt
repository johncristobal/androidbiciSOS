package com.bicisos.i7.bicisos.ui.dashboard.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bicisos.i7.bicisos.model.PolizasResponse
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.databinding.PolizaItemBinding
import com.bicisos.i7.bicisos.utils.clickPoliza

class PolizasAdapter(
    private val data: PolizasResponse,
    private val polizasFragment: Context,
    val listener : clickPoliza
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

       holder.recyclerBinding.clickView.setOnClickListener {
            listener.openPDF(data.polizas.polizasInfo[position].folio)
      }

        holder.recyclerBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return data.polizas.polizasInfo.size
    }

}