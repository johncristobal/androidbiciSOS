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
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bicisos.i7.bicisos.Model.Report

import com.bicisos.i7.bicisos.R
import com.google.firebase.database.FirebaseDatabase
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
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ReportFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    var REQUEST_CODE_CAMERA = 1
    var PICK_FROM_GALLERY = 2
    var imagesEncodedList : ArrayList<String>? = null
    var photosBool: ArrayList<Boolean>? = null

    lateinit var mBuilder : AlertDialog.Builder
    lateinit var mAlertDialog : AlertDialog
    lateinit var mDialogView : View
    var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
            listener?.onFragmentInteraction("")
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

            val database = FirebaseDatabase.getInstance()
            val reportesRef = database.getReference("reportes")

            var fotos = ""
            for(i in 0..3){
                val fotoTemp = prefs.getString("bici"+i,"null")
                //val temp = File(directory,"bici"+i+".png")
                val temp = File(fotoTemp)
                if(temp.exists()){
                    //reportesStRef!!.child("bici_"+i+".png")
                    fotos += "bici_"+i+".png,"
                }
            }

            val key = reportesRef.push().key
            reportesRef.child(key!!).setValue(Report(key,reportNombre.text.toString(),ReporteSerie.text.toString(),ReporteDesc.text.toString(),1,dateFinal,fotos)).addOnSuccessListener {
                prefs.edit().putString("reportado","1").apply()
            }.addOnFailureListener {
                Log.e("error","No se pudo subir archivo: "+it.stackTrace)
            }

            val storage = FirebaseStorage.getInstance().getReference()
            val reportesStRef: StorageReference? = storage.child("reportes").child(key)

            val wrapper = ContextWrapper(context)
            val directory = wrapper.getDir("imageDir", Context.MODE_PRIVATE);
            for(i in 0..3){
                val fotoTemp = prefs.getString("bici"+i,"null")
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
            }

            listener?.onFragmentInteraction("listo")

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
            if (fotos.equals("null"))
            {
                try {
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PICK_FROM_GALLERY);
                    } else {
                        //open view to get pictures...get gallery and pic max 4 photos
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent, "Selecciona las fotos de tu bici"), REQUEST_CODE_CAMERA)
                    }
                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }
            else{
                //ya tengo fotos...entonces cargo fotos
                val wrapper = ContextWrapper(context)
                val directory = wrapper.getDir("imageDir", Context.MODE_PRIVATE);
                /*for(i in 0..3){
                    val temp = File(directory,"bici"+i+".png")
                    if(temp.exists()){
                        imagesEncodedList!![i] = temp.absolutePath
                    }
                }*/
                for(i in 0..3){
                    val fotoTemp = prefs.getString("bici"+i,"null")
                    if (!fotoTemp!!.equals("null")){
                        imagesEncodedList!![i] = fotoTemp
                    }
                    /*val temp = File(directory,"bici"+i+".png")
                    if(temp.exists()){
                        imagesEncodedList!![i] = temp.absolutePath
                    }*/
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
        try {
            // When an Image is picked
            var imageEncoded: String

            if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && null != data) {
                // Get the Image from data

                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (data.clipData != null) {
                    val mClipData = data.clipData
                    val mArrayUri = ArrayList<Uri>()
                    for (i in 0 until mClipData!!.itemCount) {

                        val item = mClipData.getItemAt(i)
                        val uri = item.uri
                        mArrayUri.add(uri)

                        // Get the cursor
                        val cursor = activity!!.getContentResolver().query(uri, filePathColumn, null, null, null)

                        // Move to first row
                        cursor.moveToFirst()

                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        imageEncoded = cursor.getString(columnIndex)
                        if (index != -1) {
                            imagesEncodedList!![index] = imageEncoded
                        }else{
                            imagesEncodedList!![i] = (imageEncoded)
                        }
                        cursor.close()
                    }

                    //show view with photos
                    loadPohots()
                } else {
                    if (data.data != null) {

                        val mImageUri = data.data

                        // Get the cursor
                        val cursor = activity!!.getContentResolver().query(
                            mImageUri,
                            filePathColumn, null, null, null
                        )

                        // Move to first row
                        cursor.moveToFirst()

                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        imageEncoded = cursor.getString(columnIndex)

                        if (index != -1) {
                            imagesEncodedList!![index] = imageEncoded
                        }else{
                            imagesEncodedList!![0]=(imageEncoded)
                        }
                        cursor.close()

                        loadPohots()
                    }
                }
            } else {
                Toast.makeText(activity!!, "Selecciona una imagen...", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(activity!!, "Algo salio mal...", Toast.LENGTH_LONG).show()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun loadPohots(){
        mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.photos, null)

        //event when click item
        val click = View.OnClickListener{

            index = it.tag.toString().toInt()

            //preguntar por borrar foto o tomar nueva
            if (photosBool!![it.tag.toString().toInt()]){

                val alertanother = AlertDialog.Builder(activity!!)
                alertanother.setTitle("Tu bici...")
                val options = arrayOf<CharSequence>("Elegir otra foto", "Borrar foto", "Cancelar")
                alertanother.setItems(options) { dialog, item ->

                    if (options[item] == "Elegir otra foto") {
                        dialog.dismiss()
                        val intent = Intent()
                        intent.type = "image/*"
                        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        intent.action = Intent.ACTION_GET_CONTENT
                        mAlertDialog.dismiss()
                        startActivityForResult(Intent.createChooser(intent, "Selecciona la foto de tu bici"), REQUEST_CODE_CAMERA)

                    } else if (options[item] == "Borrar foto") {

                        val imgFile = File(imagesEncodedList!![index])
                        if(imgFile.exists()){
                            imgFile.delete()
                        }

                        imagesEncodedList!![index] = ""
                        when (index) {
                            0 -> {
                                photosBool!![0] = false
                                mDialogView.bici1.setImageResource(R.drawable.cameraicon)
                            }
                            1 -> {
                                photosBool!![1] = false
                                mDialogView.bici2.setImageResource(R.drawable.cameraicon)
                            }
                            2 -> {
                                photosBool!![2] = false
                                mDialogView.bici3.setImageResource(R.drawable.cameraicon)
                            }
                            3 -> {
                                photosBool!![3] = false
                                mDialogView.bici4.setImageResource(R.drawable.cameraicon)
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
                //si la foto no esta, abrimos galeria de una
                val intent = Intent()
                intent.type = "image/*"
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                mAlertDialog.dismiss()
                startActivityForResult(Intent.createChooser(intent, "Selecciona la foto de tu bici"), REQUEST_CODE_CAMERA)
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

            val directory = wrapper.getDir("imageDir", Context.MODE_PRIVATE);
            if (!directory.exists()) {
                directory.mkdir();
            }
            var i = 0

            val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
            imagesEncodedList!!.forEach {
                try {
                    val imgFile = File(it)
                    if (imgFile.exists()) {
                        /*val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
                        val temp = File(directory,"bici"+i+".png")
                        /*if(temp.exists()){
                            temp.delete()
                        }*/
                        try {
                            val fos = FileOutputStream(temp)
                            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                            fos.flush()
                            fos.close()
                        } catch (e: Exception) {
                            Log.e("SAVE_IMAGE", e.message, e)
                        }

                        val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                        editor.putString("fotos","1")
                        editor.apply()*/

                        editor.putString("bici"+i,imgFile.getAbsolutePath())
                        editor.putString("fotos","1")
                        editor.apply()

                    }else{

                    }
                }catch(e:Exception){
                    Log.w("tag","error al guardar archivo")
                }
                i++
            }
        }

        //cargarFotos()
        var i = -1
        imagesEncodedList!!.forEach {
            try {
                if (!it.equals("")){
                    val imgFile = File(it)
                    i++

                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    //options.inSampleSize = calculateInSampleSize(options,mDialogView.bici1.width,mDialogView.bici1.height)
                    //options.inJustDecodeBounds = false

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
                            Picasso.with(context!!)// .get()
                                .load(imgFile)
                                .resize(finalW, finalH)
                                //.centerCrop()
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
                            Picasso.with(context!!)// .get()
                                .load(imgFile)
                                .resize(finalW,finalH)
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
                            Picasso.with(context!!)// .get()
                                .load(imgFile)
                                .resize(finalW, finalH)
                                .into(mDialogView.bici3)
                        }
                        3 -> {
                            photosBool!![3] = true
                            Picasso.with(context!!)// .get()
                                .load(imgFile)
                                .resize(finalW, finalH)
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

        mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
