package com.bicisos.i7.bicisos.ui.contract.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bicisos.i7.bicisos.Activities.CameraPhotosActivity
import com.bicisos.i7.bicisos.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_payment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var flagPhoto = false

    var LAUNCH_SECOND_ACTIVITY = 1009

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonPaymentSet.setOnClickListener {
            if(flagPhoto){
                findNavController().navigate(R.id.action_paymentFragment2_to_resumeFragment2)
            }else{
                val myIntent = Intent(activity, CameraPhotosActivity::class.java)
                startActivityForResult(myIntent,LAUNCH_SECOND_ACTIVITY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("result")
                if (result != null) {
                    Log.w("tag...",result)

                    val prefs = requireActivity().getSharedPreferences(requireActivity().getString(R.string.preferences), Context.MODE_PRIVATE)
                    prefs.edit().putString("payment_photo",result).apply()

                    flagPhoto = true
                    buttonPaymentSet.text = "Continuar"
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
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
         * @return A new instance of fragment PaymentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PaymentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}