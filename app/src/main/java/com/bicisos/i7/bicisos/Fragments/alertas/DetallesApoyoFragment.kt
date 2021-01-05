package com.bicisos.i7.bicisos.Fragments.alertas

import android.content.Context
import android.net.Uri
import android.os.Bundle
//import androidx.core.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bicisos.i7.bicisos.Fragments.FinalReporteFragment
import com.bicisos.i7.bicisos.Model.Report

import com.bicisos.i7.bicisos.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_detalles_apoyo.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "latitude"
private const val ARG_PARAM2 = "longitude"
private const val ARG_PARAM3 = "name"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetallesApoyoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetallesApoyoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetallesApoyoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var name: String? = null
    private var listener: OnFragmentInteractionListenerDetalles? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(ARG_PARAM1)
            longitude = it.getDouble(ARG_PARAM2)
            name = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalles_apoyo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*buttonRegresar.setOnClickListener {
            childFragmentManager.popBackStack()
        }*/

        buttonEnviar.setOnClickListener {
            Log.w("vamonos", "Adios fragment apoyo")
            if (editTextDesc.text.toString().equals("")) {
                Toast.makeText(activity!!, "Describe tu apoyo...", Toast.LENGTH_SHORT).show()
            } else {
                buttonsSendDataApoyo.visibility = View.GONE
                loadingViewApoyo.visibility = View.VISIBLE

                val fecha = Date()
                val stringfecha = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val dateFinal = stringfecha.format(fecha)

                //primero enviar mi bike para que este en fierbase
                //si y solo si estoy logueado
                //mando nombre, bike, ubication
                val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                val serie = prefs.getString("serie", "null")

                val database = Firebase.database
                val bikersRef = database.getReference("reportes")
                val lat = latitude
                val long = longitude
                val bici = 4 // averia

                val key = bikersRef.push().key
                bikersRef.child(key!!).setValue(
                    Report(
                        key,
                        name!!,
                        serie!!,
                        editTextDesc.text.toString(),
                        1,
                        dateFinal,
                        "sinfotos",
                        bici,
                        lat!!,
                        long!!
                    )
                ).addOnSuccessListener {
                    //listener?.onFragmentInteractionDetalles("listo")
                    //childFragmentManager.beginTransaction().remove(this).commit()//popBackStack()

                    containerOkApo.visibility = View.VISIBLE
                    viewDataSendApo.visibility = View.INVISIBLE
                    childFragmentManager.beginTransaction().replace(R.id.containerOkApo,
                        FinalReporteFragment.newInstance("","")).commit()

                }.addOnFailureListener {
                    Log.e("error", "No se pudo subir archivo: " + it.stackTrace)
                    buttonsSendDataApoyo.visibility = View.VISIBLE
                    loadingViewApoyo.visibility = View.GONE

                    Toast.makeText(activity!!,"Tuvimos un problema. Intenta más tarde.",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteractionDetalles("")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerDetalles) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListenerDetalles")
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
    interface OnFragmentInteractionListenerDetalles {
        // TODO: Update argument type and name
        fun onFragmentInteractionDetalles(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetallesApoyoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double, param3: String) =
            DetallesApoyoFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, param1)
                    putDouble(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}
