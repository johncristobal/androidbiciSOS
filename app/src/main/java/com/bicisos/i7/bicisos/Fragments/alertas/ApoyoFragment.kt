package com.bicisos.i7.bicisos.Fragments.alertas

import android.content.Context
import android.net.Uri
import android.os.Bundle
//import androidx.core.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.fragment_apoyo.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "latitude"
private const val ARG_PARAM2 = "longitude"
private const val ARG_PARAM3 = "name"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ApoyoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ApoyoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class ApoyoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var name: String? = null

    private var listener: OnFragmentInteractionListenerApoyo? = null

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
        return inflater.inflate(R.layout.fragment_apoyo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRegresar.setOnClickListener {
            //listener?.onFragmentAveria(this)
            childFragmentManager.beginTransaction().remove(this).commit()//popBackStack()
        }

        imageViewValla.setOnClickListener {
            val detailsFrag = DetallesApoyoFragment.newInstance(latitude!!,longitude!!,name!!)
            childFragmentManager.beginTransaction()
                .addToBackStack("detalles")
                .replace(R.id.containerDetalles,detailsFrag)
                .commit()

        }

        buttonApoyo.setOnClickListener {
            val detailsFrag = DetallesApoyoFragment.newInstance(latitude!!,longitude!!,name!!)
            childFragmentManager.beginTransaction()
                .addToBackStack("detalles")
                .replace(R.id.containerDetalles,detailsFrag)
                .commit()
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteractionApoyo("")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerApoyo) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListenerApoyo")
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
    interface OnFragmentInteractionListenerApoyo {
        // TODO: Update argument type and name
        fun onFragmentInteractionApoyo(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ApoyoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double, param3: String) =
            ApoyoFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, param1)
                    putDouble(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}
