package com.bicisos.i7.bicisos.Fragments

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
//import androidx.core.app.Fragment
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
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import com.bicisos.i7.bicisos.Fragments.alertas.ApoyoFragment
import com.bicisos.i7.bicisos.Fragments.alertas.CicloviaFragment
import com.bicisos.i7.bicisos.Fragments.alertas.HelpFragment
import com.bicisos.i7.bicisos.Model.Report
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
    var cicloFrag = CicloviaFragment()
    var helpFrag = HelpFragment()
    var apoyoFrag = ApoyoFragment()
    var alertaFrag = ReportFragment()

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

// =================== action ciclvia ==================================================================================
        imageViewCiclovia.setOnClickListener {
            cicloFrag = CicloviaFragment.newInstance(latitud!!,longitud!!,name!!)

            val exitFade = Fade()
            exitFade.setDuration(MOVE_DEFAULT_TIME)
            this.setExitTransition(exitFade)

            val enterTransitionSet = TransitionSet()
            enterTransitionSet.addTransition(TransitionInflater.from(activity).inflateTransition(android.R.transition.move))
            enterTransitionSet.setDuration(MOVE_DEFAULT_TIME)
            enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME)
            cicloFrag.setSharedElementEnterTransition(enterTransitionSet)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val enterFade = Slide(Gravity.RIGHT)
                //enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME)
                enterFade.setDuration(FADE_DEFAULT_TIME)
                cicloFrag.setEnterTransition(enterFade)
            }

            val manager = childFragmentManager.beginTransaction()
                .addToBackStack("ciclovia")
                .addSharedElement(imageViewAveria,"ciclovia")
                .replace(R.id.containerAlertasCustom,cicloFrag)
            manager.commitAllowingStateLoss()
        }

// =================== action help 911 ==================================================================================
        imageViewHelp.setOnClickListener {
            helpFrag = HelpFragment.newInstance(latitud!!,longitud!!,name!!)

            val exitFade = Fade()
            exitFade.setDuration(MOVE_DEFAULT_TIME)
            this.setExitTransition(exitFade)

            val enterTransitionSet = TransitionSet()
            enterTransitionSet.addTransition(TransitionInflater.from(activity).inflateTransition(android.R.transition.move))
            enterTransitionSet.setDuration(MOVE_DEFAULT_TIME)
            enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME)
            helpFrag.setSharedElementEnterTransition(enterTransitionSet)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val enterFade = Slide(Gravity.END)
                //enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME)
                enterFade.setDuration(FADE_DEFAULT_TIME)
                helpFrag.setEnterTransition(enterFade)
            }

            val manager = childFragmentManager.beginTransaction()
                .addToBackStack("help")
                .addSharedElement(imageViewHelp,"help")
                .replace(R.id.containerAlertasCustom,helpFrag)
            manager.commitAllowingStateLoss()
        }

// =================== action apoyo ==================================================================================
        imageViewApoyo.setOnClickListener {
            apoyoFrag = ApoyoFragment.newInstance(latitud!!,longitud!!,name!!)

            val exitFade = Fade()
            exitFade.setDuration(MOVE_DEFAULT_TIME)
            this.setExitTransition(exitFade)

            val enterTransitionSet = TransitionSet()
            enterTransitionSet.addTransition(TransitionInflater.from(activity).inflateTransition(android.R.transition.move))
            enterTransitionSet.setDuration(MOVE_DEFAULT_TIME)
            enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME)
            apoyoFrag.setSharedElementEnterTransition(enterTransitionSet)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val enterFade = Slide(Gravity.END)
                //enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME)
                enterFade.setDuration(FADE_DEFAULT_TIME)
                apoyoFrag.setEnterTransition(enterFade)
            }

            val manager = childFragmentManager.beginTransaction()
                .addToBackStack("apoyo")
                .addSharedElement(imageViewApoyo,"apoyo")
                .replace(R.id.containerAlertasCustom,apoyoFrag)
            manager.commitAllowingStateLoss()
        }

// =================== action añlerta ==================================================================================
        imageViewAlerta.setOnClickListener {

            val preferences = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            val reportado = preferences.getString("reportado","null")
            //Si es null, no se ha reportado, reporte..
            if(reportado!!.equals("null")) {

                preferences.edit().putString("fromReporte", "alertaFragment").apply()

                alertaFrag = ReportFragment.newInstance(latitud!!, longitud!!, name!!)

                val exitFade = Fade()
                exitFade.setDuration(MOVE_DEFAULT_TIME)
                this.setExitTransition(exitFade)

                val enterTransitionSet = TransitionSet()
                enterTransitionSet.addTransition(TransitionInflater.from(activity).inflateTransition(android.R.transition.move))
                enterTransitionSet.setDuration(MOVE_DEFAULT_TIME)
                enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME)
                alertaFrag.setSharedElementEnterTransition(enterTransitionSet)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val enterFade = Slide(Gravity.END)
                    //enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME)
                    enterFade.setDuration(FADE_DEFAULT_TIME)
                    alertaFrag.setEnterTransition(enterFade)
                }

                val manager = childFragmentManager.beginTransaction()
                    .addToBackStack("report")
                    .addSharedElement(imageViewAlerta, "report")
                    .replace(R.id.containerAlertasCustom, alertaFrag)
                manager.commitAllowingStateLoss()
            }else{
                if(reportado.equals("1")){
                    //ya hay reporte,
                    val llavereporte = preferences.getString("llavereporte","null")
                    if(!llavereporte!!.equals("null")){

                        //tenemos la llave de reporte...
                        /*val manager = childFragmentManager.beginTransaction()
                            .addToBackStack("report")
                            .addSharedElement(imageViewAlerta, "report")
                            .replace(R.id.containerAlertasCustom, alertaFrag)
                        manager.commitAllowingStateLoss()*/

                        val reference = FirebaseDatabase.getInstance().getReference("reportes").child(llavereporte)
                        reference.addListenerForSingleValueEvent(object : ValueEventListener{

                            override fun onCancelled(p0: DatabaseError) {
                                Log.w("No","sin resultados")
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                preferences.edit().putString("detalleMapFragment","1").apply()
                                preferences.edit().putString("fromAlerta","1").apply()

                                val detailtFrag = DetailReportFragment.newInstance(p0.getValue(Report::class.java)!!)
                                childFragmentManager.beginTransaction()
                                    .addToBackStack("detalles")
                                    .replace(R.id.containerAlertasCustom,detailtFrag)
                                    .commit()
                            }
                        })
                    }
                }
            }
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
