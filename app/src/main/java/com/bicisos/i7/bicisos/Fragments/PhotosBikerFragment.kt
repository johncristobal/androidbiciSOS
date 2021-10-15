package com.bicisos.i7.bicisos.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import com.bicisos.i7.bicisos.Activities.CameraPhotosActivity
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.utils.Constants
import com.bicisos.i7.bicisos.utils.Event
import com.bicisos.i7.bicisos.utils.photosViewModel
import com.bumptech.glide.Glide

private const val ARG_COLOR = "arg_color"
private const val ARG_ROBO = "arg_robo"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotosBikerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotosBikerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var color_param: Int? = null
    private var robo_bici: Boolean = false

    lateinit var view_fragment: View

    var imagesEncodedList : ArrayList<String>? = null
    var LAUNCH_SECOND_ACTIVITY = 1009

    lateinit var bici_frontal: ImageView
    lateinit var bici_trasea: ImageView
    lateinit var bici_derecha: ImageView
    lateinit var bici_izquierda: ImageView
    lateinit var aceptarActionPhotosButton: Button
    lateinit var imageTempView : ImageView

    var index: Int = -1
    var bici = -1
    var photosBool: ArrayList<Boolean>? = null

    private val viewModel: photosViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            color_param = it.getInt(ARG_COLOR)
            robo_bici = it.getBoolean(ARG_ROBO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        view_fragment = inflater.inflate(R.layout.fragment_photos_biker, container, false)

        photosBool = ArrayList()
        photosBool!!.add(false)
        photosBool!!.add(false)
        photosBool!!.add(false)
        photosBool!!.add(false)
        index = -1

        bici_frontal = view_fragment.findViewById(R.id.bici1)
        bici_trasea = view_fragment.findViewById(R.id.bici2)
        bici_izquierda = view_fragment.findViewById(R.id.bici3)
        bici_derecha = view_fragment.findViewById(R.id.bici4)
        aceptarActionPhotosButton = view_fragment.findViewById(R.id.aceptarActionPhotos)

        val linear: LinearLayout = view_fragment.findViewById(R.id.borderLinear)
        if(color_param == Constants.GTT_Seguros){
            linear.background = ResourcesCompat.getDrawable(resources, R.drawable.back_gtt_photos, null)
            aceptarActionPhotosButton.background = ResourcesCompat.getDrawable(resources, R.drawable.backgoogle, null)
            aceptarActionPhotosButton.setTextColor(ResourcesCompat.getColor(resources, R.color.blanco, null))

            val textView6: TextView = view_fragment.findViewById(R.id.textView6)
            textView6.setTextColor(ResourcesCompat.getColor(resources, R.color.blanco, null))
        }

        val prefs = requireActivity().getSharedPreferences(requireActivity().getString(R.string.preferences), Context.MODE_PRIVATE)
        val set = prefs.getString("photos", null)
        if (set == null)
        {
            imagesEncodedList = ArrayList()
            imagesEncodedList!!.add("a")
            imagesEncodedList!!.add("b")
            imagesEncodedList!!.add("c")
            imagesEncodedList!!.add("d")
        }
        else{
            imagesEncodedList = set.split(",").toCollection(ArrayList())
            for (i in imagesEncodedList!!.size..3){
                imagesEncodedList!!.add("x")
            }
        }

        loadPohots()

        bici_frontal.setOnClickListener { picture_action(0, it as ImageView) }
        bici_trasea.setOnClickListener { picture_action(1, it as ImageView) }
        bici_izquierda.setOnClickListener { picture_action(2, it as ImageView) }
        bici_derecha.setOnClickListener { picture_action(3, it as ImageView) }

        // aceptar action
        aceptarActionPhotosButton.setOnClickListener {

            var photosString = ""
            val editor = requireActivity().getSharedPreferences(
                getString(R.string.preferences),
                Context.MODE_PRIVATE
            ).edit()

            if(robo_bici){
                for (item in imagesEncodedList!!) {
                    if (item.length > 1)
                        photosString += item + ","
                }
                photosString = photosString.dropLast(1)
                editor.putString("photos", photosString)
                editor.apply()

                viewModel._uploadUI.value = Event("1")
            }else {
                var requieredPhotos = true

                for (item in imagesEncodedList!!) {
                    if (item.length > 1)
                        photosString += item + ","
                    else {
                        requieredPhotos = false
                    }
                }

                if (!requieredPhotos) {
                    viewModel._uploadUI.value = Event("-1")
                } else {
                    photosString = photosString.dropLast(1)
                    editor.putString("photos", photosString)
                    editor.apply()

                    viewModel._uploadUI.value = Event("1")
                }
            }
        }

        return view_fragment
    }

    fun loadPohots(){
        var i = -1
        imagesEncodedList!!.forEach {
            try {
                if (it.length > 1){
                    val imgFile = it
                    i++

                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true

                    when (i) {
                        0 -> {
                            photosBool!![0] = true

                            Glide.with(requireActivity())
                                .load(imgFile)
                                .into(bici_frontal)
                        }
                        1 -> {
                            photosBool!![1] = true
                            Glide.with(requireActivity())
                                .load(imgFile)
                                .into(bici_trasea)
                        }
                        2 -> {
                            photosBool!![2] = true
                            Glide.with(requireActivity())
                                .load(imgFile)
                                .into(bici_izquierda)
                        }
                        3 -> {
                            photosBool!![3] = true
                            Glide.with(requireActivity())
                                .load(imgFile)
                                .into(bici_derecha)
                        }
                    }
                }
            }catch (e: Exception){
                Log.w("warning","error al elegir archivo")
            }
        }
    }

    fun picture_action(elem: Int, item: ImageView){

        imageTempView = item
        index = elem

        if ((imagesEncodedList!![index].length > 1)){
            val alertanother = AlertDialog.Builder(requireActivity())
            alertanother.setTitle("Tu bici...")
            val options = arrayOf<CharSequence>("Tomar otra foto", "Borrar foto", "Cancelar")
            alertanother.setItems(options) { dialog, item ->

                if (options[item] == "Tomar otra foto") {
                    dialog.dismiss()
                    takePhoto()

                } else if (options[item] == "Borrar foto") {

                    when (index) {
                        0 -> {
                            photosBool!![0] = false
                            bici_frontal.setImageResource(R.drawable.bici_lateral)
                            imagesEncodedList!![index] = "a"
                        }
                        1 -> {
                            photosBool!![1] = false
                            bici_trasea.setImageResource(R.drawable.bici_manubrio)
                            imagesEncodedList!![index] = "b"
                        }
                        2 -> {
                            photosBool!![2] = false
                            bici_izquierda.setImageResource(R.drawable.bici_sillin)
                            imagesEncodedList!![index] = "c"
                        }
                        3 -> {
                            photosBool!![3] = false
                            bici_derecha.setImageResource(R.drawable.bici_pedal)
                            imagesEncodedList!![index] = "d"
                        }
                    }
                    dialog.dismiss()
                } else if (options[item] == "Cancelar") {
                    dialog.dismiss()
                }
            }
            val alert = alertanother.create()
            alert.show()
        }else{
            takePhoto()
        }
    }

    private fun takePhoto() {
        val myIntent = Intent(activity, CameraPhotosActivity::class.java)
        startActivityForResult(myIntent,LAUNCH_SECOND_ACTIVITY)
    }

    //CODE - onactivityresult ==========================================================================
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("result")
                if (result != null) {
                    Log.w("tag...",result)
                    imagesEncodedList!![index] = result
                    //get uri data, show photo
                    Glide.with(requireActivity())
                        .load(result)
                        .into(imageTempView)
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
         * @return A new instance of fragment PhotosBikerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: Boolean = false) =
            PhotosBikerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLOR, param1)
                    putBoolean(ARG_ROBO, param2)
                }
            }
    }
}