package com.bicisos.i7.bicisos.Fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bicisos.i7.bicisos.Adapters.CustomBici

import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.fragment_personaliza.*
import android.content.Intent
import android.widget.Toast
import android.content.ClipData
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.photos.view.*
import org.jetbrains.anko.AlertDialogBuilder
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.File
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

    public var REQUEST_CODE_CAMERA = 1
    public var imagesEncodedList : ArrayList<String>? = null

    lateinit var mBuilder : AlertDialog.Builder
    lateinit var mAlertDialog : AlertDialog

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

        listaimagenes.layoutManager = LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false)

        val imagenes = ArrayList<Int>()
        imagenes.add(R.mipmap.bicia)
        imagenes.add(R.mipmap.bicib)
        imagenes.add(R.mipmap.bicic)
        imagenes.add(R.mipmap.bicid)

        val adapter = CustomBici(activity!!,imagenes)

        listaimagenes.adapter = adapter

        buttonAceptar.setOnClickListener {
            listener?.onFragmentInteraction("")
        }

        buttonDespues.setOnClickListener {
            listener?.onFragmentInteraction("")
        }

        imagesEncodedList = ArrayList<String>()
        takePictures.setOnClickListener {
            //open view to get pictures...get gallery and pic max 4 photos
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Selecciona las fotos de tu bici"), REQUEST_CODE_CAMERA)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            // When an Image is picked
            var imageEncoded: String
            imagesEncodedList!!.clear()

            if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && null != data) {
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
                        imagesEncodedList!!.add(imageEncoded)
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
                        imagesEncodedList!!.add(imageEncoded)
                        cursor.close()

                        loadPohots()
                    }
                }
            } else {
                Toast.makeText(activity!!, "You haven't picked Image",Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(activity!!, "Something went wrong", Toast.LENGTH_LONG).show()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun loadPohots(){

        val click = View.OnClickListener{
            //preguntar por borrar foto o tomar nueva
            Log.e("tag",it.tag.toString())
            val alertanother = AlertDialog.Builder(activity!!)
            alertanother.setTitle("Tomar foto")

            alertanother.setPositiveButton("Tomar otra foto", DialogInterface.OnClickListener { dialogInterface, i ->
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Selecciona las fotos de tu bici"), REQUEST_CODE_CAMERA)

            })
            alertanother.setNegativeButton("Borrar foto", DialogInterface.OnClickListener { dialogInterface, i ->

            })
            alertanother.setNeutralButton("Cancelar", DialogInterface.OnClickListener { dialogInterface, i ->

            })

            val alert = alertanother.create()
            alert.show()
        }

        val mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.photos, null)

        //AlertDialogBuilder
        mBuilder = AlertDialog.Builder(activity!!).setView(mDialogView)

        mDialogView.bici1.setOnClickListener(click)
        mDialogView.bici2.setOnClickListener(click)
        mDialogView.bici3.setOnClickListener(click)
        mDialogView.bici4.setOnClickListener(click)
        mDialogView.aceptarAction.setOnClickListener {
            Log.e("tag","aceptar action")
            if (mAlertDialog != null) {
                mAlertDialog.dismiss()
            }
        }

        var i = 0
        imagesEncodedList!!.forEach {
            val imgFile = File(it)

            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())

                when (i) {
                    0 -> {
                        mDialogView.bici1.setImageBitmap(myBitmap)
                    }
                    1 -> {
                        mDialogView.bici2.setImageBitmap(myBitmap)
                    }
                    2 -> {
                        mDialogView.bici3.setImageBitmap(myBitmap)
                    }
                    3 -> {
                        mDialogView.bici4.setImageBitmap(myBitmap)
                    }
                }
                i++
            }

        }

        mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
