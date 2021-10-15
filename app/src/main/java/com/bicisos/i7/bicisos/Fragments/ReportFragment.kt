package com.bicisos.i7.bicisos.Fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bicisos.i7.bicisos.Activities.CameraPhotosActivity
import com.bicisos.i7.bicisos.Api.ServiceApi

import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.model.UserResponse
import com.bicisos.i7.bicisos.model.reportes.Reporte
import com.bicisos.i7.bicisos.model.reportes.Robery
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.utils.Constants
import com.bicisos.i7.bicisos.utils.photosViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_report.*
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import java.lang.reflect.TypeVariable


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "latitude"
private const val ARG_PARAM2 = "longitude"
private const val ARG_PARAM3 = "name"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ReportFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ReportFragment : BottomSheetDialogFragment() {
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var name: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private val job = Job()
    private val scopeMainThread = CoroutineScope(job + Dispatchers.Main)
    private val scopeIO = CoroutineScope(job + Dispatchers.IO)

    private val viewModel: photosViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(ARG_PARAM1)
            longitude = it.getDouble(ARG_PARAM2)
            name = it.getString(ARG_PARAM3)
        }
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = getScreenHeight()//your harcoded or dimen height
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)
        val linearLayout = view.findViewById<ConstraintLayout>(R.id.root)
        val params = linearLayout.layoutParams
        params.height = getScreenHeight()
        linearLayout.layoutParams = params

        return view
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().getDisplayMetrics().heightPixels
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences(requireActivity().getString(R.string.preferences), Context.MODE_PRIVATE)
        val name = prefs.getString("nombre","null")
        if (!name!!.equals("null")) {
            reportNombre.setText(name)
        }
        val serie = prefs.getString("serie","null")
        if (!serie!!.equals("null")) {
            ReporteSerie.setText(serie)
        }
        val desc = prefs.getString("desc","null")
        if (!desc!!.equals("null")) {
            ReporteDesc.setText(desc)
        }

        buttonCancelar.setOnClickListener{
            dismiss()
        }

        buttonReportar.setOnClickListener {
            val editor = prefs.edit()
            editor.putString("nombre",reportNombre.text.toString())
            editor.putString("serie",ReporteSerie.text.toString())
            editor.putString("desc",ReporteDesc.text.toString())
            editor.apply()

            val user = prefs.getString("user", "null")
            val iduser = Gson().fromJson(user, UserResponse::class.java).user.id

            val set = prefs.getString("photos", null)
            var arrayPhotos : Array<String> = arrayOf("x", "x", "x", "x")
            var fileCompleta : File? = null
            var fileManubrio : File? = null
            var fileSillin : File? = null
            var filePedal : File? = null

            if (set != null)
            {
                val images = set.split(",").toCollection(ArrayList())
                var i=0
                for(item in images){
                    if(item.length > 1){
                        when(i){
                            0 -> { fileCompleta = File(item); arrayPhotos[0] = "lateral" }
                            1 -> { fileManubrio = File(item); arrayPhotos[1] = "manubrio" }
                            2 -> { fileSillin = File(item); arrayPhotos[2] = "sillin" }
                            3 -> { filePedal = File(item); arrayPhotos[3] = "pedal" }
                        }
                        i++
                    }
                }
            }else{
                Toast.makeText(requireContext(), "No podemos reportar sin fotos", Toast.LENGTH_SHORT).show()
            }

            val arrayFiltered = arrayPhotos.filter { e ->
                !e.equals("x")
            }

            val reporte = Reporte(
                iduser,
                "1",
                "",
                latitude!!.toString(),
                longitude!!.toString(),
                ReporteDesc.text.toString(),
                Robery(
                    ReporteSerie.text.toString(),
                    arrayFiltered.toList()
                )
            )

            val dataReporte = Gson().toJson(reporte)
            val bodyData = RequestBody.create(MultipartBody.FORM, dataReporte)
            val repo = Repository(ServiceApi())

            scopeIO.launch {
                try {
                    var filePartLateral : MultipartBody.Part? = null
                    var filePartManubrio : MultipartBody.Part? = null
                    var filePartSillin : MultipartBody.Part? = null
                    var filePartPedal : MultipartBody.Part? = null

                    if(fileCompleta != null){
                        filePartLateral = MultipartBody.Part.createFormData(
                            "lateral",
                            fileCompleta.name,
                            RequestBody.create(
                                MediaType.parse("image/*"),fileCompleta)
                        )
                    }
                    if(fileManubrio != null){
                        filePartManubrio = MultipartBody.Part.createFormData(
                            "manubrio",
                            fileManubrio.name,
                            RequestBody.create(
                                MediaType.parse("image/*"),fileManubrio)
                        )
                    }
                    if(fileSillin != null){
                        filePartSillin = MultipartBody.Part.createFormData(
                            "sillin",
                            fileSillin.name,
                            RequestBody.create(
                                MediaType.parse("image/*"),fileSillin)
                        )
                    }
                    if(filePedal != null){
                        filePartPedal = MultipartBody.Part.createFormData(
                            "pedal",
                            filePedal.name,
                            RequestBody.create(
                                MediaType.parse("image/*"),filePedal)
                        )
                    }

                    repo.reporteBiciRobo(
                        filePartLateral,
                        filePartManubrio,
                        filePartSillin,
                        filePartPedal,
                        bodyData
                    )
                    scopeMainThread.launch {
                        prefs.edit().putString("reportado","1").apply()
                        containerOkReport.visibility = View.VISIBLE
                        viewDataReport.visibility = View.INVISIBLE
                        childFragmentManager.beginTransaction().replace(R.id.containerOkReport,FinalReporteFragment.newInstance("","")).commit()
                    }

                }catch (e: Exception){
                    scopeMainThread.launch {
                        Toast.makeText(requireContext(), "Error al reportar, intente mas tarde", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.uploadUI.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{
                Log.w("from...","data arriving $it")
                when(it){
                    "1" -> {
                        viewDataReport.visibility = View.VISIBLE
                        fragmentPhotosReporte.visibility = View.GONE
                    }

                }
            }
        })

        takePicturesReporte.setOnClickListener {
            fragmentPhotosReporte.visibility = View.VISIBLE
            viewDataReport.visibility = View.GONE
            val fragmentInstace = PhotosBikerFragment.newInstance(Constants.SOS_Ciclista, true)
            childFragmentManager.beginTransaction().add(R.id.fragmentPhotosReporte,fragmentInstace).commit()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double, param3: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, param1)
                    putDouble(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}
