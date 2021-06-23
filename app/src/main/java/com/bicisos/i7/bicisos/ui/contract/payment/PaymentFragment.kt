package com.bicisos.i7.bicisos.ui.contract.payment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bicisos.i7.bicisos.Activities.CameraPhotosActivity
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.fragment_payment.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var flagPhoto = false

    private var LAUNCH_SECOND_ACTIVITY = 1009
    private val LAUNCH_GALLERY = 1010
    private val PERMISSION_CODE = 1001;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonPaymentSet.setOnClickListener {
            if(flagPhoto){
                findNavController().navigate(R.id.action_paymentFragment2_to_resumeFragment2)
            }else{
                showOptions()
            }
        }
    }

    private fun showOptions(){
        val alertanother = AlertDialog.Builder(requireActivity())
        alertanother.setTitle("Tu bici...")
        val options = arrayOf<CharSequence>("Cámara", "Galería", "Cancelar")
        alertanother.setItems(options) { dialog, item ->

            if (options[item] == "Cámara") {
                dialog.dismiss()
                val myIntent = Intent(activity, CameraPhotosActivity::class.java)
                startActivityForResult(myIntent, LAUNCH_SECOND_ACTIVITY)
            } else if (options[item] == "Galería") {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    }
                    else{
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }

                dialog.dismiss()
            } else if (options[item] == "Cancelar") {
                dialog.dismiss()
            }
        }
        val alert = alertanother.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_GALLERY && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            val prefs = requireActivity().getSharedPreferences(
                requireActivity().getString(R.string.preferences),
                Context.MODE_PRIVATE
            )

            val res = getPathFromURI(imageUri!!);
            if(res != null){
                prefs.edit().putString("payment_photo", res).apply()
                flagPhoto = true
                buttonPaymentSet.text = "Continuar"
            }
        }
        if (requestCode == LAUNCH_SECOND_ACTIVITY && resultCode == Activity.RESULT_OK) {

            val result = data!!.getStringExtra("result")
            if (result != null) {

                val prefs = requireActivity().getSharedPreferences(
                    requireActivity().getString(R.string.preferences),
                    Context.MODE_PRIVATE
                )
                prefs.edit().putString("payment_photo", result).apply()

                flagPhoto = true
                buttonPaymentSet.text = "Continuar"
            }
        }

        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }

    fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireContext().getContentResolver().query(contentUri!!, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = cursor.getString(column_index)
                cursor.close()
            }
        }
        return res
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun pickImageFromGallery(){
        val gallery = Intent(Intent.ACTION_PICK)
        gallery.type = "image/*"
        startActivityForResult(gallery, LAUNCH_GALLERY)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PaymentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PaymentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}