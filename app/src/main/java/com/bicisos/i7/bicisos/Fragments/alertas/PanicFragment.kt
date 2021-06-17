package com.bicisos.i7.bicisos.Fragments.alertas

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bicisos.i7.bicisos.Fragments.FinalReporteFragment
import com.bicisos.i7.bicisos.model.Report
import com.bicisos.i7.bicisos.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_panic.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "latitude"
private const val ARG_PARAM2 = "longitude"
private const val ARG_PARAM3 = "name"

/**
 * A simple [Fragment] subclass.
 * Use the [PanicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PanicFragment : BottomSheetDialogFragment() {
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(ARG_PARAM1)
            longitude = it.getDouble(ARG_PARAM2)
            name = it.getString(ARG_PARAM3)
        }
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRegresar.setOnClickListener {
            dismiss()
        }
        buttonEnviarPanic.setOnClickListener {
            buttonEnviarPanic.visibility = View.INVISIBLE
            loadingBarPanic.visibility = View.VISIBLE
            val fecha = Date()
            val stringfecha = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val dateFinal = stringfecha.format(fecha)

            val prefs = requireActivity().getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            val serie = prefs.getString("serie", "null")

            val database = Firebase.database
            val bikersRef = database.getReference("reportes")
            val lat = latitude
            val long = longitude
            val bici = 5 // panic

            val key = bikersRef.push().key
            bikersRef.child(key!!).setValue(
                Report(
                    key,
                    name!!,
                    serie!!,
                    "Botón de pánico",
                    1,
                    dateFinal,
                    "sinfotos",
                    bici,
                    lat!!,
                    long!!
                )
            ).addOnSuccessListener {
                //listener?.onFragmentInteractionCiclovia("enviado")

                buttonEnviarPanic.visibility = View.VISIBLE
                loadingBarPanic.visibility = View.INVISIBLE

                containerOkPanic.visibility = View.VISIBLE
                viewDataSendPanic.visibility = View.INVISIBLE

                childFragmentManager.beginTransaction().replace(R.id.containerOkPanic,
                    FinalReporteFragment.newInstance("","")).commit()

            }.addOnFailureListener {
                Log.e("error", "No se pudo subir archivo: " + it.stackTrace)
                buttonEnviarPanic.visibility = View.VISIBLE
                loadingBarPanic.visibility = View.INVISIBLE

                Toast.makeText(requireActivity(),"Tuvimos un problema. Intenta más tarde.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PanicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double, param3: String) =
            PanicFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, param1)
                    putDouble(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}