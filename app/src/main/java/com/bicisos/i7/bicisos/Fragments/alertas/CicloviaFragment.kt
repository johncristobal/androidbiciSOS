package com.bicisos.i7.bicisos.Fragments.alertas

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bicisos.i7.bicisos.Api.ServiceApi
import com.bicisos.i7.bicisos.Fragments.FinalReporteFragment

import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.model.UserResponse
import com.bicisos.i7.bicisos.model.reportes.Reporte
import com.bicisos.i7.bicisos.repository.Repository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_ciclovia.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "latitude"
private const val ARG_PARAM2 = "longitude"
private const val ARG_PARAM3 = "name"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CicloviaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CicloviaFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CicloviaFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var name: String? = null
    private var listener: OnFragmentInteractionListenerCiclovia? = null

    private val job = Job()
    private val scopeMainThread = CoroutineScope(job + Dispatchers.Main)
    private val scopeIO = CoroutineScope(job + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(ARG_PARAM1)
            longitude = it.getDouble(ARG_PARAM2)
            name = it.getString(ARG_PARAM3)
        }
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ciclovia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRegresar.setOnClickListener {
            //listener?.onFragmentAveria(this)
            //childFragmentManager.beginTransaction().remove(this).commit()//popBackStack()
            dismiss()
        }

        buttonEnviar.setOnClickListener {
            if (editTextAveria.text.toString().equals("")){
                Toast.makeText(requireActivity(),"Describe la ciclovía brevemente...",Toast.LENGTH_SHORT).show()
            }else {
                buttonEnviar.visibility = View.INVISIBLE
                loadingBarCiclo.visibility = View.VISIBLE

                val prefs = requireActivity().getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                val user = prefs.getString("user", "null")
                val iduser = Gson().fromJson(user, UserResponse::class.java).user.id

                val lat = latitude
                val long = longitude
                val bici = 3 // ciclocia

                val description = editTextAveria.text.toString()

                val repo = Repository(ServiceApi())
                scopeIO.launch {
                    try{
                        val user = repo.reporteBici(
                            Reporte(iduser, bici.toString(), "",lat.toString(),long.toString(), description, null)
                        )
                        scopeMainThread.launch {
                            buttonEnviar.visibility = View.VISIBLE
                            loadingBarCiclo.visibility = View.INVISIBLE

                            containerOkCiclo.visibility = View.VISIBLE
                            viewDataSendCiclo.visibility = View.INVISIBLE

                            childFragmentManager.beginTransaction().replace(R.id.containerOkCiclo, FinalReporteFragment.newInstance("","")).commit()
                        }
                    }catch(e: Exception){
                        scopeMainThread.launch {
                            Log.e("error", "Error al enviar info: " + e.message)
                            buttonEnviar.visibility = View.VISIBLE
                            loadingBarCiclo.visibility = View.INVISIBLE

                            Toast.makeText(requireActivity(),"Tuvimos un problema. Intenta más tarde.",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerCiclovia) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
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
    interface OnFragmentInteractionListenerCiclovia {
        // TODO: Update argument type and name
        fun onFragmentInteractionCiclovia(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CicloviaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double, param3: String) =
            CicloviaFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, param1)
                    putDouble(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}
