package com.bicisos.i7.bicisos.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bicisos.i7.bicisos.Adapters.CustomPager
import com.bicisos.i7.bicisos.Model.Report

import com.bicisos.i7.bicisos.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_detail_report.*
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailReportFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetailReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var id: String? = null
    private var fotos: String? = null
    private var report: Report? = null
    private var listener: FragmentDetalleListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            report = it.getSerializable(ARG_PARAM1) as Report
            //id = it.getString(ARG_PARAM1)
            //fotos = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.w("id", report!!.id)

        //recuperar las fotos de storage
        val imagenes = ArrayList<String>()

        val pictures = report!!.fotos.split(",")
        pictures.forEach {
            if(!it.isEmpty()) {
                imagenes.add(it)
            }
        }

        viewpager.layoutManager = LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false)
        val adapter = CustomPager(activity!!,imagenes, report!!.id)
        viewpager.adapter = adapter

        buttonCerrar.setOnClickListener {
            listener?.detalleInteraction("")
        }

        textViewNombreDetalle.setText(report!!.name)
        textViewSerieDetalle.setText(report!!.serie)
        textViewDescripcionDetalle.setText(report!!.description)

        buttonShare.setOnClickListener {
            //open share activity
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentDetalleListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface FragmentDetalleListener {
        // TODO: Update argument type and name
        fun detalleInteraction(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Report) =
            DetailReportFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}
