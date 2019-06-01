package com.bicisos.i7.bicisos.Fragments

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.Slide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bicisos.i7.bicisos.Fragments.alertas.AveriaFragment

import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.fragment_alerta.*
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.transition.Fade
import android.view.Gravity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "latitud"
private const val ARG_PARAM2 = "longitud"
private const val ARG_PARAM3 = "name"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AlertaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AlertaFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AlertaFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var latitud: Double? = null
    private var longitud: Double? = null
    private var name: String? = null
    private var listener: OnFragmentAlertasListener? = null

    var averiaFrag = AveriaFragment()

    val MOVE_DEFAULT_TIME: Long = 1000
    private val FADE_DEFAULT_TIME: Long = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitud = it.getDouble(ARG_PARAM1)
            longitud = it.getDouble(ARG_PARAM2)
            name = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewCerrar.setOnClickListener {
            listener?.onFragmentInteractionAlertas("cerrar")
            //childFragmentManager.beginTransaction().remove(this).commit()
        }

        imageViewAveria.setOnClickListener {
            averiaFrag = AveriaFragment.newInstance(latitud!!,longitud!!,name!!)

            val exitFade = Fade()
            exitFade.setDuration(MOVE_DEFAULT_TIME)
            this.setExitTransition(exitFade)

            val enterTransitionSet = TransitionSet()
            enterTransitionSet.addTransition(TransitionInflater.from(activity).inflateTransition(android.R.transition.move))
            enterTransitionSet.setDuration(MOVE_DEFAULT_TIME)
            enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME)
            averiaFrag.setSharedElementEnterTransition(enterTransitionSet)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val enterFade = Slide(Gravity.RIGHT)
                //enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME)
                enterFade.setDuration(FADE_DEFAULT_TIME)
                averiaFrag.setEnterTransition(enterFade)
            }

            val manager = childFragmentManager.beginTransaction()
                .addToBackStack("averia")
                .addSharedElement(imageViewAveria,"averia")
                .replace(R.id.containerAlertasCustom,averiaFrag)
            manager.commitAllowingStateLoss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentAlertasListener) {
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
    interface OnFragmentAlertasListener {
        // TODO: Update argument type and name
        fun onFragmentInteractionAlertas(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlertaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double, param3: String) =
            AlertaFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, param1)
                    putDouble(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}
