package com.bicisos.i7.bicisos.Fragments.alertas

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.fragment_help.*
import org.jetbrains.anko.makeCall

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "latitude"
private const val ARG_PARAM2 = "longitude"
private const val ARG_PARAM3 = "name"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HelpFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HelpFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelpFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var name: String? = null
    private var listener: OnFragmentInteractionListenerHelp? = null

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
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRegresar.setOnClickListener {
            //listener?.onFragmentAveria(this)
            childFragmentManager.beginTransaction().remove(this).commit()//popBackStack()
        }

        buttonHelp.setOnClickListener {
            activity!!.makeCall("911")
        }

        textViewC5.setOnClickListener {
            openTwwiter("https://twitter.com/C5_CDMX")

        }

        textViewSSP.setOnClickListener {
            openTwwiter("https://twitter.com/SSP_CDMX")

        }

        textViewUCS.setOnClickListener {
            openTwwiter("https://twitter.com/UCS_GCDMX")

        }

        textViewPGJDF.setOnClickListener {
            openTwwiter("https://twitter.com/PGJDF_CDMX")

        }

    }

    fun openTwwiter(url: String){

        try {
            // get the Twitter app if possible
            activity!!.packageManager.getPackageInfo("com.twitter.android", 0)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            // no Twitter app, revert to browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteractionHelp(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerHelp) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListenerHelp")
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
    interface OnFragmentInteractionListenerHelp {
        // TODO: Update argument type and name
        fun onFragmentInteractionHelp(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double, param3: String) =
            HelpFragment().apply {
                arguments = Bundle().apply {
                        putDouble(ARG_PARAM1, param1)
                        putDouble(ARG_PARAM2, param2)
                        putString(ARG_PARAM3, param3)
                }
            }
    }
}
