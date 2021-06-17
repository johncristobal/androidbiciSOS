package com.bicisos.i7.bicisos.ui.dashboard.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bicisos.i7.bicisos.Api.ServiceApi
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.databinding.PolizasFragmentBinding
import com.bicisos.i7.bicisos.repository.Repository
import kotlinx.android.synthetic.main.polizas_fragment.*

class PolizasFragment : Fragment() {

    companion object {
        fun newInstance() = PolizasFragment()
    }

    private lateinit var viewModel: PolizasViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding: PolizasFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.polizas_fragment, container, false)
        val repo = Repository(ServiceApi())
        val factory = PolizasFactory(repo)

        viewModel = ViewModelProvider(this, factory).get(PolizasViewModel::class.java)
        binding.polizasViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.polizas.observe(viewLifecycleOwner, { data ->
            insurancesList.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                if (data != null) {
                    it.adapter = PolizasAdapter(data,requireContext())
                }else{
                    Log.e("Error","No data in the sesoin")
                }
            }
        })
    }
}
