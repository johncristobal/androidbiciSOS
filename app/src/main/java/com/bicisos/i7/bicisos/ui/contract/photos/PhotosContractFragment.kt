package com.bicisos.i7.bicisos.ui.contract.photos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bicisos.i7.bicisos.Fragments.PhotosBikerFragment
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.utils.Constants.Companion.GTT_Seguros
import com.bicisos.i7.bicisos.utils.photosViewModel
import com.google.android.material.snackbar.Snackbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotosContractFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotosContractFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: photosViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentInstace = PhotosBikerFragment.newInstance(GTT_Seguros)
        childFragmentManager.beginTransaction().add(R.id.fragmentPhotos,fragmentInstace).commit()

        viewModel.uploadUI.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                Log.w("from...","data arriving $it")
                when(it){
                    "-1" -> {
                        Snackbar.make(view, "Favor de subir las cuatro fotos de tu bici para continuar", Snackbar.LENGTH_LONG).show();
                    }
                    else -> {

                    }
                }
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotosContractFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotosContractFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}