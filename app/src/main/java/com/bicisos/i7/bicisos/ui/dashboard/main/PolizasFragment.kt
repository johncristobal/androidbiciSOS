package com.bicisos.i7.bicisos.ui.dashboard.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bicisos.i7.bicisos.Api.ApiUrls
import com.bicisos.i7.bicisos.Api.ServiceApi
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.databinding.PolizasFragmentBinding
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.clickPoliza
import kotlinx.android.synthetic.main.polizas_fragment.*


class PolizasFragment : Fragment(), clickPoliza {

    companion object {
        fun newInstance() = PolizasFragment()
    }

    private lateinit var viewModel: PolizasViewModel
    private val REQUEST_PHONE_CODE = 1009

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: PolizasFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.polizas_fragment,
            container,
            false
        )

        val prefs = requireActivity().getSharedPreferences(
            getString(R.string.preferences),
            Context.MODE_PRIVATE
        )
        val phone = prefs.getString("phone", "null")

        val repo = Repository(ServiceApi())
        val factory = PolizasFactory(repo, phone!!)

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
                    val filter = data.polizas.polizasInfo.filter { p -> p.isActive }
                    data.polizas.polizasInfo = filter
                    it.adapter = PolizasAdapter(data, requireContext(), this)
                } else {
                    Log.e("Error", "No data in the sesoin")
                }
            }
        })

        imageViewPanic.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                    arrayOf(
                        Manifest.permission.CALL_PHONE,
                    ),
                    REQUEST_PHONE_CODE
                )
            }else {
                val intent = Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:" + resources.getString(R.string.tel_gtt))
                )
                startActivity(intent)
            }
        }
    }

    override fun openPDF(folio: String) {
        val urlImg = ApiUrls.urlApi+"/"+folio+"/poliza.pdf"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlImg))
        startActivity(browserIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            REQUEST_PHONE_CODE -> when {

                grantResults.isEmpty() ->
                    Toast.makeText(requireActivity(), "Permiso denegado.", Toast.LENGTH_SHORT)
                        .show()

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    val intent = Intent(
                        Intent.ACTION_CALL,
                        Uri.parse("tel:" + resources.getString(R.string.tel_gtt))
                    )
                    startActivity(intent)
                }

                else -> {
                    // Permission denied.
                    Toast.makeText(requireActivity(), "Permiso denegado.", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }
}
