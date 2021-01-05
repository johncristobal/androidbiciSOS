package com.bicisos.i7.bicisos.Fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
//import androidx.core.app.Fragment
import androidx.core.content.ContextCompat
//import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bicisos.i7.bicisos.Activities.CameraPhotosActivity
import com.bicisos.i7.bicisos.Model.Report

import com.bicisos.i7.bicisos.R
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.photos.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

    var REQUEST_CODE_CAMERA = 1
    var LAUNCH_SECOND_ACTIVITY = 1009
    var PICK_FROM_GALLERY = 2
    var imagesEncodedList : ArrayList<String>? = null
    var photosBool: ArrayList<Boolean>? = null

    lateinit var mBuilder : AlertDialog.Builder
    lateinit var mAlertDialog : AlertDialog
    lateinit var mDialogView : View
    lateinit var imageTempView : ImageView
    var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(ARG_PARAM1)
            longitude = it.getDouble(ARG_PARAM2)
            name = it.getString(ARG_PARAM3)
        }
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = activity!!.getSharedPreferences(activity!!.getString(R.string.preferences), Context.MODE_PRIVATE)
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

//            val preferences = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
//            val fromReporte = preferences.getString("fromReporte","null")
//
//            if (fromReporte.equals("null")){
//
//            }else if (fromReporte.equals("reporteActivity")){
//                listener?.onFragmentInteraction("")
//            }else{
//                childFragmentManager.beginTransaction().remove(this).commit()//popBackStack()
//            }
            dismiss()
        }

        buttonReportar.setOnClickListener {
            val editor = prefs.edit()
            editor.putString("nombre",reportNombre.text.toString())
            editor.putString("serie",ReporteSerie.text.toString())
            editor.putString("desc",ReporteDesc.text.toString())
            editor.apply()

            val fecha = Date()
            val stringfecha = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val dateFinal = stringfecha.format(fecha)

            val database = Firebase.database
            val reportesRef = database.getReference("reportes")

            val set = prefs.getString("photos", null)
            var fotos = ""
            if (set != null)
            {
                val images = set.split(",").toCollection(ArrayList())
                var i=0
                for(item in images){
                    if(item.length > 1){
                        fotos += "bici_"+i+".png,"
                        i++
                    }
                }
            }else{
                fotos = ""
            }

//            for(i in 0..3){
//                val fotoTemp = prefs.getString("bici"+i,"null")
//                //val temp = File(directory,"bici"+i+".png")
//                val temp = File(fotoTemp)
//                if(temp.exists()){
//                    //reportesStRef!!.child("bici_"+i+".png")
//                    fotos += "bici_"+i+".png,"
//                }
//            }

            val key = reportesRef.push().key
            reportesRef.child(key!!).setValue(Report(key,reportNombre.text.toString(),ReporteSerie.text.toString(),ReporteDesc.text.toString(),1,dateFinal,fotos,1,latitude!!,longitude!!)).addOnSuccessListener {
                prefs.edit().putString("reportado","1").apply()
                Log.d("success","Reporte hecho")
            }.addOnFailureListener {
                Log.e("error","No se pudo subir archivo: "+it.stackTrace)
            }

            val storage = FirebaseStorage.getInstance().getReference()
            val reportesStRef: StorageReference? = storage.child("reportes").child(key)

            if (set != null)
            {
                val images = set.split(",").toCollection(ArrayList())
                var i = 0
                for(item in images){
                    val fotoTemp = item//prefs.getString("bici"+i,"null")
                    //val temp = File(directory,"bici"+i+".png")
                    val temp = File(fotoTemp)
                    if(temp.exists()){
                        //reportesStRef!!.child("bici_"+i+".png")
                        val stream = FileInputStream(File(temp.absolutePath))

                        val taski = reportesStRef!!.child("bici_"+i+".png").putStream(stream)
                        taski.addOnFailureListener{
                            Log.e("error","No se pudo subir archivo: "+temp.absolutePath)
                        }.addOnSuccessListener {

                        }
                    }
                    i++
                }
            }

            containerOkReport.visibility = View.VISIBLE
            viewDataReport.visibility = View.INVISIBLE
            childFragmentManager.beginTransaction().replace(R.id.containerOkReport,FinalReporteFragment.newInstance("","")).commit()

            //listener?.onFragmentInteraction("listo")
        }

        photosBool = ArrayList<Boolean>()
        photosBool!!.add(false)
        photosBool!!.add(false)
        photosBool!!.add(false)
        photosBool!!.add(false)

        index = -1

        imagesEncodedList = ArrayList<String>()
        imagesEncodedList!!.add("")
        imagesEncodedList!!.add("")
        imagesEncodedList!!.add("")
        imagesEncodedList!!.add("")

        takePicturesReporte.setOnClickListener {
            val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            val fotos : String = editor.getString("fotos","null")!!
            //val set = prefs.getStringSet("photos", null)
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
                //ya tengo fotos...entonces cargo fotos
                //val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)

                imagesEncodedList = set.split(",").toCollection(ArrayList())
                for (i in imagesEncodedList!!.size..3){
                    imagesEncodedList!!.add("x")
                }

                loadPohots()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE_CAMERA){
            if(permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[0] == (PackageManager.PERMISSION_GRANTED))
            {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Selecciona las fotos de tu bici"), REQUEST_CODE_CAMERA)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("result")
                Log.w("tag...",result)
                imagesEncodedList!![index] = result
                //get uri data, show photo
                Glide.with(activity!!)
                    .load(result)
                    .into(imageTempView)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    //CODE: - open layout photos =======================================================================
    fun loadPohots(){
        mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.photos, null)

        var i = -1

        imagesEncodedList!!.forEach {
            try {
                if (it.length > 1){
                    val imgFile = it
                    i++

                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true

                    val widht = options.outWidth
                    val height = options.outHeight
                    var finalW = 0
                    var finalH = 0
                    if (widht > height){
                        finalW = 250
                        finalH = 200
                    }else if(widht == height){
                        finalW = 250
                        finalH = 250
                    }else{
                        finalW = 200
                        finalH = 250
                    }

                    when (i) {
                        0 -> {
                            photosBool!![0] = true
                            /*Glide
                                .with(activity!!)
                                .load(myBitmap)
                                .override(mDialogView.bici1.width, mDialogView.bici1.height) // resizes the image to these dimensions (in pixel)
                                .centerCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                                .into(mDialogView.bici1);*/
                            //mDialogView.bici1.setImageBitmap(compressedBitmap)
//                            Picasso.with(context!!)// .get()
//                                .load(imgFile)
//                                .resize(finalW, finalH)
//                                //.centerCrop()
//                                .into(mDialogView.bici1)
                            Glide.with(activity!!)
                                .load(imgFile)
                                .into(mDialogView.bici1)
                        }
                        1 -> {
                            photosBool!![1] = true
                            /*Glide
                                .with(activity!!)
                                .load(myBitmap)
                                .override(mDialogView.bici2.width, mDialogView.bici2.height) // resizes the image to these dimensions (in pixel)
                                .centerCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                                .into(mDialogView.bici2);*/
                            //mDialogView.bici2.setImageBitmap(compressedBitmap)

                            Glide.with(activity!!)
                                .load(imgFile)
                                .into(mDialogView.bici2)
                        }
                        2 -> {
                            photosBool!![2] = true
                            /*Glide
                                .with(activity!!)
                                .load(myBitmap)
                                .override(mDialogView.bici3.width, mDialogView.bici3.height) // resizes the image to these dimensions (in pixel)
                                .centerCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                                .into(mDialogView.bici3);*/
                            //mDialogView.bici3.setImageBitmap(compressedBitmap)
                            Glide.with(activity!!)
                                .load(imgFile)
                                .into(mDialogView.bici3)
                        }
                        3 -> {
                            photosBool!![3] = true
                            Glide.with(activity!!)
                                .load(imgFile)
                                .into(mDialogView.bici4)
                            //mDialogView.bici4.setImageBitmap(compressedBitmap)
                            /*Glide
                                .with(activity!!)
                                .load(myBitmap)
                                .override(mDialogView.bici4.width, mDialogView.bici4.height) // resizes the image to these dimensions (in pixel)
                                .centerCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                                .into(mDialogView.bici4);*/
                        }
                    }
                }
            }catch (e: Exception){
                Log.w("warning","error al elegir archivo")
            }
        }
        //event when click item
        val click = View.OnClickListener{

            when (it.id) {
                R.id.bici1 -> {
                    index = 0
                }R.id.bici2 -> {
                index = 1
            }R.id.bici3 -> {
                index = 2
            }R.id.bici4 -> {
                index = 3
            }else -> {
                index = -1
            }
            }
            imageTempView = it as ImageView

            //index = it.tag.toString().toInt()

            //preguntar por borrar foto o tomar nueva
            if ((imagesEncodedList!![index].length > 1)){

                val alertanother = AlertDialog.Builder(activity!!)
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
                                mDialogView.bici1.setImageResource(R.drawable.cameraicon)
                                imagesEncodedList!![index] = "a"
                            }
                            1 -> {
                                photosBool!![1] = false
                                mDialogView.bici2.setImageResource(R.drawable.cameraicon)
                                imagesEncodedList!![index] = "b"
                            }
                            2 -> {
                                photosBool!![2] = false
                                mDialogView.bici3.setImageResource(R.drawable.cameraicon)
                                imagesEncodedList!![index] = "c"
                            }
                            3 -> {
                                photosBool!![3] = false
                                mDialogView.bici4.setImageResource(R.drawable.cameraicon)
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

        //AlertDialogBuilder
        mBuilder = AlertDialog.Builder(activity!!).setView(mDialogView)
        val wrapper = ContextWrapper(context)

        mDialogView.bici1.setOnClickListener(click)
        mDialogView.bici2.setOnClickListener(click)
        mDialogView.bici3.setOnClickListener(click)
        mDialogView.bici4.setOnClickListener(click)

        mDialogView.aceptarAction.setOnClickListener {
            Log.e("tag","aceptar action--guardando fotos en carpeta de app ")
            mAlertDialog.dismiss()

            val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
            var photosString = ""
            for(item in imagesEncodedList!!){
                photosString += item+","
            }
            photosString = photosString.dropLast(1)

            editor.putString("photos",photosString)
            editor.apply()

        }

        mAlertDialog = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun takePhoto() {
        val myIntent = Intent(activity, CameraPhotosActivity::class.java)
        startActivityForResult(myIntent,LAUNCH_SECOND_ACTIVITY)
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
