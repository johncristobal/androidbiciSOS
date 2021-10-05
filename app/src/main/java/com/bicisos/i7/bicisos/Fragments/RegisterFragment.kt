package com.bicisos.i7.bicisos.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.bicisos.i7.bicisos.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*
//import jdk.nashorn.internal.runtime.ECMAException.getException
import com.google.firebase.auth.FirebaseUser
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.firebase.auth.AuthResult
import androidx.annotation.NonNull
import android.R.attr.password
import android.util.Log
import com.bicisos.i7.bicisos.Api.ServiceApi
import com.bicisos.i7.bicisos.model.RegisterBicis
import com.bicisos.i7.bicisos.repository.Repository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//import jdk.nashorn.internal.runtime.ECMAException.getException




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegisterFragment.OnFragmentInteractionListenerRegister] interface
 * to handle interaction events.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListenerRegister? = null

    private val job = Job()
    private val scopeMainThread = CoroutineScope(job + Dispatchers.Main)
    private val scopeIO = CoroutineScope(job + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewCloseRegister.setOnClickListener {
            listener?.onFragmentInteractionRegister("")
            //childFragmentManager.beginTransaction().remove(this)
        }

        buttonIngresarRegistro.setOnClickListener {
            //recupèramos datos de los campos, validamos y hacemos el auth fikrebasde
            progressBarRegister.visibility = View.VISIBLE
            buttonIngresarRegistro.text = ""

            val name = editTextName.text.toString()
            val mail = editTextCorreo.text.toString()
            val pass = editTextPass.text.toString()

            if(!validarDatos()){
                progressBarRegister.visibility = View.INVISIBLE
                buttonIngresarRegistro.text = "Registrarse"
            }
            else {
                val repo = Repository(ServiceApi())
                scopeIO.launch {
                    try {
                        val user = repo.registerBicis(RegisterBicis(
                            name, mail, pass, "123"
                        ))
                        scopeMainThread.launch {
                            progressBarRegister.visibility = View.INVISIBLE
                            buttonIngresarRegistro.text = "Registrarse"

                            val editor = requireActivity().getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                            editor.putString("sesion", "1")
                            editor.putString("reloadData", "1")
                            editor.putString("nombre", user.user.nombre)
                            editor.putString("user", Gson().toJson(user))
                            editor.apply()

                            listener?.onFragmentInteractionRegister("login")
                        }
                    }catch (e: Exception){
                        scopeMainThread.launch {
                            progressBarRegister.visibility = View.INVISIBLE
                            buttonIngresarRegistro.text = "Registrarse"
                            Toast.makeText(
                                activity,
                                e.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
//                val mAuth = FirebaseAuth.getInstance()
//                mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(object: OnCompleteListener<AuthResult>{
//                    override fun onComplete(task: Task<AuthResult>) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("TAG", "createUserWithEmail:success")
//                            val user = mAuth.currentUser
//                            val editor = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
//                            editor.putString("sesion","1")
//                            editor.putString("reloadData","1")
//                            editor.putString("nombre",name)
//                            editor.apply()
//                            listener?.onFragmentInteractionRegister("login")
//                            //updateUI(user)
//                        } else {
//                            progressBarRegister.visibility = View.INVISIBLE
//                            buttonIngresarRegistro.text = "Registrarse"
//
//                            // If sign in fails, display a message to the user.
//                            Log.w("TAG", "createUserWithEmail:failure", task.getException())
//                            try {
//                                throw task.exception!!
//                            } catch (weakPassword: FirebaseAuthWeakPasswordException) {
//                                //Log.d(TAG, "onComplete: weak_password")
//                                Toast.makeText(activity, "La contraseña es incorrecta....", Toast.LENGTH_SHORT).show()
//                            } catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
//                                Toast.makeText(activity, "Validar correo....", Toast.LENGTH_SHORT).show()
//                            } catch (existEmail: FirebaseAuthUserCollisionException) {
//                                Toast.makeText(activity, "El correo ya existe, inicia sesión...", Toast.LENGTH_SHORT).show()
//                            } catch (e: Exception) {
//                                Toast.makeText(activity, "Error al crear usuario, intente más tarde...", Toast.LENGTH_SHORT).show()
//                            }
//                            // if user enters wrong email.
//                            // if user enters wrong password.
//                            //updateUI(null)
//                        }
//                    }
//                })
            }
        }
    }

    private fun validarDatos(): Boolean {
        val name = editTextName.text.toString()
        val mail = editTextCorreo.text.toString()
        val pass = editTextPass.text.toString()
        val pas2 = editTextPassConfirm.text.toString()
        if(name.equals("")){
            Toast.makeText(activity,"Favor de colocar nombre...",Toast.LENGTH_SHORT).show()
            return false
        }else if(mail.equals("")){
            Toast.makeText(activity,"Favor de colocar correo...",Toast.LENGTH_SHORT).show()
            return false
        }else if(pass.equals("")){
            Toast.makeText(activity,"Favor de colocar contraseña...",Toast.LENGTH_SHORT).show()
            return false
        }else if(!pass.equals(pas2)){
            Toast.makeText(activity,"Las contraseñas no coinciden...",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteractionRegister("")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerRegister) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListenerRegister")
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
    interface OnFragmentInteractionListenerRegister {
        // TODO: Update argument type and name
        fun onFragmentInteractionRegister(uri: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
