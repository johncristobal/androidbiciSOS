package com.bicisos.i7.bicisos.Fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
//import androidx.core.app.Fragment
//import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bicisos.i7.bicisos.Adapters.CustomPager
import com.bicisos.i7.bicisos.model.Report

import com.bicisos.i7.bicisos.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_detail_report.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailReportFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetailReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var id: String? = null
    private var fotos: String? = null
    private var report: Report? = null
    private var listener: FragmentDetalleListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            report = it.getSerializable(ARG_PARAM1) as Report
            //id = it.getString(ARG_PARAM1)
            //fotos = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val storage = FirebaseStorage.getInstance()
        Log.w("id", report!!.id)

        //recuperar las fotos de storage
        val imagenes = ArrayList<String>()
        val uris = ArrayList<Uri>()

        val pictures = report!!.fotos.split(",")
        loadingShare.visibility = View.VISIBLE
        viewpager.visibility = View.INVISIBLE
        var contTemp = 0
        pictures.forEach {
            if(!it.isEmpty()) {
                imagenes.add(it)
                contTemp++

                val storageRef = storage.reference.child("reportes").child(report!!.id).child(it)
                val tempName = it
                storageRef.downloadUrl.addOnSuccessListener { uriTemp ->
                    Glide.with(this)
                        .asBitmap()
                        .load(uriTemp)
                        .into(object : CustomTarget<Bitmap>(){
                            override fun onLoadCleared(placeholder: Drawable?) {

                            }

                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                                try {
                                    val file = File(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${tempName}.jpg");
                                    val stream = FileOutputStream(file);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                    stream.close();
                                    val uri = Uri.fromFile(file)
                                    uris.add(uri)

                                    if (uris.size == imagenes.size){
                                        loadingShare.visibility = View.GONE
                                        viewpager.visibility = View.VISIBLE

                                    }
                                } catch (e: IOException) {
                                    Log.d("TAG", "IOException while trying to write file for sharing: " + e);
                                    loadingShare.visibility = View.GONE
                                    viewpager.visibility = View.VISIBLE
                                }
                            }
                        })

                    //.override(100,200)
                }.addOnFailureListener {
                    loadingShare.visibility = View.GONE
                    viewpager.visibility = View.VISIBLE
                }
            }
        }

        viewpager.layoutManager = LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false)
        val adapter = CustomPager(activity!!,imagenes, report!!.id)
        viewpager.adapter = adapter

        buttonCerrar.setOnClickListener {
            val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            if(prefs.getString("detalleMapFragment","null").equals("null")){

                listener?.detalleInteraction("")
            }else{
                prefs.edit().putString("detalleMapFragment","null").apply()
                childFragmentManager.beginTransaction().remove(this).commit()//popBackStack()
                listener?.detalleInteraction("")
            }
        }

        textViewNombreDetalle.setText(report!!.name)
        textViewSerieDetalle.setText(report!!.serie)
        textViewDescripcionDetalle.setText(report!!.description)

        buttonShare.setOnClickListener {
            //open share activity
            //buttonShare.visibility = View.INVISIBLE
            //loadingShare.visibility = View.VISIBLE

            if (imagenes.size > 0){
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND_MULTIPLE
                shareIntent.type = "image/*"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Detalles de bici: ${report!!.description}")
                shareIntent.putExtra(Intent.EXTRA_TITLE, "Reporte de robo: ${report!!.serie}")
                //shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                //shareIntent.putExtra(Intent.EXTRA_STREAM, uris)
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris)
                startActivity(Intent.createChooser(shareIntent, "Compartir reporte..."))

                //buttonShare.visibility = View.VISIBLE
               // loadingShare.visibility = View.GONE

                /*val storageRef = storage.reference.child("reportes").child(report!!.id).child(imagenes[0])

                storageRef.downloadUrl.addOnSuccessListener {
                    /*val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    shareIntent.type = "image..."
                    shareIntent.putExtra(Intent.EXTRA_STREAM, it)
                    //shareIntent.putExtra(Intent.EXTRA_TEXT, report!!.description)
                    startActivity(Intent.createChooser(shareIntent, "Compartir con..."))*/

                    Glide.with(this)
                        .asBitmap()
                        .load(it.toString())
                        .into(object : CustomTarget<Bitmap>(){
                            override fun onLoadCleared(placeholder: Drawable?) {

                            }

                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                                try {
                                    val file = File(activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${UUID.randomUUID()}.jpg");
                                    val stream = FileOutputStream(file);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                    stream.close();
                                    val uri = Uri.fromFile(file)

                                    val shareIntent = Intent()
                                    shareIntent.action = Intent.ACTION_SEND
                                    shareIntent.type = "image/*"
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Detalles de bici: ${report!!.description}")
                                    shareIntent.putExtra(Intent.EXTRA_TITLE, "Reporte de robo: ${report!!.serie}")
                                    //shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, uris)
                                    startActivity(Intent.createChooser(shareIntent, "Compartir reporte..."))

                                    buttonShare.visibility = View.VISIBLE
                                    loadingShare.visibility = View.GONE

                                } catch (e: IOException) {
                                    Log.d("TAG", "IOException while trying to write file for sharing: " + e);
                                    buttonShare.visibility = View.VISIBLE
                                    loadingShare.visibility = View.GONE
                                }

                                /*val wrapper = ContextWrapper(activity!!)

                                // Initialize a new file instance to save bitmap object
                                var file = wrapper.getDir("Images",Context.MODE_PRIVATE)
                                file = File(file,"${UUID.randomUUID()}.jpg")

                                try{
                                    // Compress the bitmap and save in jpg format
                                    val stream: OutputStream = FileOutputStream(file)
                                    resource.compress(Bitmap.CompressFormat.JPEG,100,stream)
                                    stream.flush()
                                    stream.close()
                                }catch (e: IOException){
                                    e.printStackTrace()
                                }

                                val shareIntent = Intent()
                                shareIntent.action = Intent.ACTION_SEND
                                shareIntent.type = "iamge/*"
                                //shareIntent.putExtra(Intent.EXTRA_TEXT, report!!.description)
                                //shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                                shareIntent.putExtra(Intent.EXTRA_STREAM, it)
                                startActivity(Intent.createChooser(shareIntent, "Compartir reporte..."))*/*/
                            }
                        })

                        //.override(100,200)
                }.addOnFailureListener {
                    buttonShare.visibility = View.VISIBLE
                    loadingShare.visibility = View.GONE
                }*/*/
            }else{
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "image/*"
                shareIntent.putExtra(Intent.EXTRA_TEXT, report!!.description)
                //shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                startActivity(Intent.createChooser(shareIntent, "Compartir reporte..."))

                //buttonShare.visibility = View.VISIBLE
                //loadingShare.visibility = View.GONE

            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentDetalleListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener") as Throwable
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
    interface FragmentDetalleListener {
        // TODO: Update argument type and name
        fun detalleInteraction(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Report) =
            DetailReportFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}
