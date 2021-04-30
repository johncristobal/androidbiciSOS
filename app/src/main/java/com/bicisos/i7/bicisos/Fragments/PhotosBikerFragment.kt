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
import android.widget.ImageView
import com.bicisos.i7.bicisos.Activities.CameraPhotosActivity
import com.bicisos.i7.bicisos.R
import com.bumptech.glide.Glide
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

    lateinit var view_fragment: View

    var imagesEncodedList : ArrayList<String>? = null
    var LAUNCH_SECOND_ACTIVITY = 1009

    lateinit var bici_frontal: ImageView
    lateinit var bici_trasea: ImageView
    lateinit var bici_derecha: ImageView
    lateinit var bici_izquierda: ImageView
    lateinit var imageTempView : ImageView

    var index: Int = -1
    var bici = -1
    var photosBool: ArrayList<Boolean>? = null

    private var listener: photosSaved? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

        aceptarActionPhotos.setOnClickListener { listener?.saveDataArrayPhotos(this.imagesEncodedList) }
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
                            bici_frontal.setImageResource(R.drawable.cameraicon)
                            imagesEncodedList!![index] = "a"
                        }
                        1 -> {
                            photosBool!![1] = false
                            bici_trasea.setImageResource(R.drawable.cameraicon)
                            imagesEncodedList!![index] = "b"
                        }
                        2 -> {
                            photosBool!![2] = false
                            bici_izquierda.setImageResource(R.drawable.cameraicon)
                            imagesEncodedList!![index] = "c"
                        }
                        3 -> {
                            photosBool!![3] = false
                            bici_derecha.setImageResource(R.drawable.cameraicon)
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
                Log.w("tag...",result)
                imagesEncodedList!![index] = result
                //get uri data, show photo
                Glide.with(requireActivity())
                    .load(result)
                    .into(imageTempView)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    interface photosSaved{
        fun saveDataArrayPhotos(data: ArrayList<String>?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is photosSaved) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement photosSaved")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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