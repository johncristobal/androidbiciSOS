package com.bicisos.i7.bicisos.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.fragment_photos_biker.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotosBikerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotosBikerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var imagesEncodedList : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photos_biker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences(requireActivity().getString(R.string.preferences), Context.MODE_PRIVATE)
        val editor = requireActivity().getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        val set = prefs.getString("photos", null)
        if (set == null)
        {
            imagesEncodedList = ArrayList<String>()
            imagesEncodedList!!.add("a")
            imagesEncodedList!!.add("b")
            imagesEncodedList!!.add("c")
            imagesEncodedList!!.add("d")

            loadPohots()

        }
        else{

            imagesEncodedList = set.split(",").toCollection(ArrayList())
            for (i in imagesEncodedList!!.size..3){
                imagesEncodedList!!.add("x")
            }

            loadPohots()
        }
    }

    fun sendData(view: View){
        Log.w("bici1","Click para el primer icono photos frag")
    }

    fun loadPohots(){

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotosBikerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotosBikerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}