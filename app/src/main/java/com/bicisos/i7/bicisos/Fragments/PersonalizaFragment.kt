package com.bicisos.i7.bicisos.Fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.os.Bundle
//import androidx.core.app.Fragment
//import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bicisos.i7.bicisos.Adapters.CustomBici

import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.fragment_personaliza.*
import android.widget.Toast
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.photos.view.*
import org.jetbrains.anko.AlertDialogBuilder
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.os.Environment
import android.text.format.Time
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photos.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files.exists

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PersonalizaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PersonalizaFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PersonalizaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnPersonalizaListener? = null

    var REQUEST_PERMISSION = 100
    var photoFile: File? = null
    var REQUEST_CODE_CAMERA = 1
    var PICK_FROM_GALLERY = 2
    var imagesEncodedList : ArrayList<String>? = null
    var photosBool: ArrayList<Boolean>? = null

    lateinit var mBuilder : AlertDialog.Builder
    lateinit var mAlertDialog : AlertDialog
    lateinit var mDialogView : View

    lateinit var imageTempView : ImageView

    var index: Int = -1
    var bici = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personaliza, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                REQUEST_PERMISSION
            )
        }

        /*if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION
            )
        }*/

        val prefs = activity!!.getSharedPreferences(activity!!.getString(R.string.preferences), Context.MODE_PRIVATE)
        val name = prefs.getString("nombre","null")
        if (!name!!.equals("null")) {
            editTextNombrePer.setText(name)
        }
        val serie = prefs.getString("serie","null")
        if (!serie!!.equals("null")) {
            editTextSerie.setText(serie)
        }
        val desc = prefs.getString("desc","null")
        if (!desc!!.equals("null")) {
            editTextDesc.setText(desc)
        }
        bici = prefs.getInt("bici",-1)

        listaimagenes.layoutManager = LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false)

        val imagenes = ArrayList<Int>()
        imagenes.add(R.mipmap.bicia)
        imagenes.add(R.mipmap.bicib)
        imagenes.add(R.mipmap.bicic)
        imagenes.add(R.mipmap.bicid)
        imagenes.add(R.mipmap.bicie)
        imagenes.add(R.mipmap.bicif)

        val adapter = CustomBici(activity!!,imagenes,bici)

        listaimagenes.adapter = adapter

        buttonAceptar.setOnClickListener {

            //antes de finishm tenemos que guardar el num de serie, la bici que eligio, las caracteristicas
            //bici la guardamos al dar clic
            //salvemos lo demas
            bici = prefs.getInt("bici",-1)
            if (bici == -1) {
                Toast.makeText(activity!!, "Selecciona una bici de la lista para continuar...",Toast.LENGTH_LONG).show()
            }else {
                val editor = prefs.edit()
                editor.putString("nombre", editTextNombrePer.text.toString());
                editor.putString("serie", editTextSerie.text.toString());
                editor.putString("desc", editTextDesc.text.toString());
                editor.apply()

                listener?.onFragmentInteraction("")
            }
        }

        buttonDespues.setOnClickListener {
            bici = prefs.getInt("bici",-1)
            if (bici == -1) {
                Toast.makeText(activity!!, "Selecciona una bici de la lista para continuar...",Toast.LENGTH_LONG).show()
            }else {
                val editor = prefs.edit()
                editor.putString("nombre", editTextNombrePer.text.toString());
                editor.putString("serie", editTextSerie.text.toString());
                editor.putString("desc", editTextDesc.text.toString());
                editor.apply()

                listener?.onFragmentInteraction("")
            }
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

        takePictures.setOnClickListener {
            val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            val fotos : String = editor.getString("fotos","null")!!
            if (fotos.equals("null"))
            {
                loadPohots()
                /*try {
                    if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                }*/*/
            }
            else{
                //ya tengo fotos...entonces cargo fotos
                //val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                val wrapper = ContextWrapper(context)
                val directory = wrapper.getDir("imageDir", Context.MODE_PRIVATE);
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
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Selecciona foto de tu bici"), REQUEST_CODE_CAMERA)
            }
        }
    }

//CODE: - open layout photos =======================================================================
    fun loadPohots(){
        mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.photos, null)

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
            alertaPhoto()
        }

        //AlertDialogBuilder
        mBuilder = AlertDialog.Builder(activity!!).setView(mDialogView)
        val wrapper = ContextWrapper(context)

        mDialogView.bici1.setOnClickListener(click)
        mDialogView.bici2.setOnClickListener(click)
        mDialogView.bici3.setOnClickListener(click)
        mDialogView.bici4.setOnClickListener(click)

        var i = -1

        imagesEncodedList!!.forEach {
            try {
                if (!it.equals("")){

                    //val imgFile = File(it)
                    i++

                    val options = BitmapFactory.Options()
                    options.inSampleSize = 8
                    //val bitmap = BitmapFactory.decodeFile(it,options)
                    val bitmap = BitmapFactory.decodeFile(it)

                    when (i) {
                        0 -> {
                            photosBool!![0] = true

                            Glide.with(activity!!)
                                .load(bitmap)
                                .into(mDialogView.bici1)
                        }
                        1 -> {
                            photosBool!![1] = true

                            Glide.with(activity!!)
                                .load(bitmap)
                                .into(mDialogView.bici2)
                        }
                        2 -> {
                            photosBool!![2] = true

                            Glide.with(activity!!)
                                .load(bitmap)
                                .into(mDialogView.bici3)
                        }
                        3 -> {
                            photosBool!![3] = true
                            Glide.with(activity!!)
                                .load(bitmap)
                                .into(mDialogView.bici4)
                        }
                    }
                }
            }catch (e: Exception){
                Log.w("warning","error al elegir archivo")
            }
        }

    mDialogView.aceptarAction.setOnClickListener {

        Log.e("tag","aceptar action--guardando fotos en carpeta de app ")
        mAlertDialog.dismiss()

        val directory = wrapper.getDir("imageDir", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir()
        }
        var i = 0

        val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()

        //save paths into sharepreferences
        imagesEncodedList!!.forEach {
            try {
                val imgFile = File(it)
                if (imgFile.exists()) {

                    editor.putString("bici"+i,imgFile.getAbsolutePath())
                    editor.putString("fotos","1")
                    editor.apply()

                }else{
                    editor.putString("bici"+i,"null")
                    editor.apply()
                }
            }catch(e:Exception){
                Log.w("tag","error al guardar archivo")
            }
            i++
        }
    }

    mAlertDialog = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

// CODE: - Open alerta para seleccionar camara, galeria, cancelar===================================
    private fun alertaPhoto(){
        val alertanother = AlertDialog.Builder(activity!!)
        alertanother.setTitle("Sube fotos de tu bici...")
        val options = arrayOf<CharSequence>("Cámara","Galería", "Cancelar")

        alertanother.setItems(options) { dialog, item ->

            if (options[item] == "Galería") {

                //Check permission again to ensure it
                if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        REQUEST_PERMISSION
                    )
                } else {

                    dialog.dismiss()
                    val intent = Intent()
                    intent.type = "image/*"
                    //val mimeTypes = Array<String>({"image/jpeg","image/png"})
                    //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent.action = Intent.ACTION_PICK
                    startActivityForResult(Intent.createChooser(intent,"Selecciona la foto de tu bici"), PICK_FROM_GALLERY)
                }

            } else if (options[item] == "Cámara") {

                if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA
                        ),
                        REQUEST_PERMISSION
                    )
                } else {
                    val time = Time()
                    time.setToNow()
                    val nametine = java.lang.Long.toString(time.toMillis(false))
                    takePicture( nametine)

                }
            } else if (options[item] == "Cancelar") {
                dialog.dismiss()
            }
        }

        val alert = alertanother.create()
        alert.show()
    }

    fun takePicture(name: String) {
        val takepic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //startActivityForResult(i, FRONT_VEHICLE);
        if (takepic.resolveActivity(activity!!.packageManager) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile(name)
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File...
                Log.e("tag","error al crear archivo ")
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity!!, "com.bicisos.i7.bicisos.fileprovider", photoFile!!)
                //val photoURI = Uri.fromFile(photoFile)
                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takepic, REQUEST_CODE_CAMERA)
            } else {
                Toast.makeText(activity!!, "Tuvimos un problema al tomar la imagen. Intente mas tarde.", Toast.LENGTH_SHORT).show()
            }
        }
    }

//CODE - onactivityresult ==========================================================================
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            var imageEncoded: String = ""
            if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (data.clipData != null) {
                    //val mClipData = data.clipData
                    val mImageUri = data.clipData.getItemAt(0).uri

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
                    /*val mArrayUri = ArrayList<Uri>()
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
                    }*/

                    Glide.with(activity!!)
                        .load(imageEncoded)
                        .into(imageTempView)

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

                        imagesEncodedList!![index] = imageEncoded
                        Glide.with(activity!!)
                            .load(mImageUri)
                            .into(imageTempView)
                    }
                }
            } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
                // Get the Image from camera
                //val prefs= activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
                //val filePath = prefs.getString("nombrefotofrontal","null") //mCurrentPhotoPath
                val options = BitmapFactory.Options()
                options.inSampleSize = 8
                val bitmap = BitmapFactory.decodeFile(photoFile!!.path,options)

                val ei = ExifInterface(photoFile!!.path)
                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

                var rotatedBitmap : Bitmap? = null;
                when(orientation) {

                    ExifInterface.ORIENTATION_ROTATE_90 -> {
                        rotatedBitmap = rotateImage(bitmap, 90.0F)
                    }

                    ExifInterface.ORIENTATION_ROTATE_180 -> {
                        rotatedBitmap = rotateImage(bitmap, 180.0f)
                    }

                    ExifInterface.ORIENTATION_ROTATE_270 -> {
                        rotatedBitmap = rotateImage(bitmap, 270.0f)
                    }

                    ExifInterface.ORIENTATION_NORMAL -> {
                        rotatedBitmap = bitmap
                    }
                    else -> {
                        rotatedBitmap = bitmap;
                    }
                }

                try {
                    val time = Time()
                    time.setToNow()
                    val nametine = java.lang.Long.toString(time.toMillis(false))

                    val photoRotated = createImageFile(nametine)
                    val out = FileOutputStream(photoRotated)
                    rotatedBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, out);
                    imagesEncodedList!![index] = photoRotated!!.absolutePath
                }catch (e: Exception){
                    e.printStackTrace()
                    imagesEncodedList!![index] = photoFile!!.absolutePath
                }

                //the best wasy glide
                Glide.with(activity!!)
                    .load(rotatedBitmap)
                    .into(imageTempView)


            } else {
                Toast.makeText(activity!!, "Selecciona una imagen...",Toast.LENGTH_LONG).show()
            }
        }

    catch (e: Exception) {
            Toast.makeText(activity!!, "Algo salio mal...", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun rotateImage(source: Bitmap, i: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(i)
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
            matrix, true)
    }

    @Throws(IOException::class)
    private fun createImageFile(username: String): File? {
        // Create an image file name
        try {
            val image: File
            Log.w("data getfilesdir",activity!!.getFilesDir().absolutePath)


            if (!File(activity!!.getFilesDir(), "images").exists()){
                File(activity!!.getFilesDir(), "images").mkdirs()
            }
            image = File(activity!!.getFilesDir(), "images" + File.separator + username + ".png")

            if (image.exists()) {
                image.delete()
            }

            return image
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPersonalizaListener) {
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
    interface OnPersonalizaListener {
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
         * @return A new instance of fragment PersonalizaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalizaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
