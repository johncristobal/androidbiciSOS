package com.bicisos.i7.bicisos.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bicisos.i7.bicisos.Api.ServiceApi
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.databinding.LoginViewModelFragmentBinding
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.ui.dashboard.DashboardGttActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.login_view_model_fragment.*

class LoginViewModelFragment : Fragment() {

    companion object {
        fun newInstance() = LoginViewModelFragment()
    }

    private lateinit var viewModel: LoginViewModelViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.login_view_model_fragment, container, false)
        val binding : LoginViewModelFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.login_view_model_fragment, container, false)
        val repo = Repository(ServiceApi())
        val factory = LoginViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory).get(LoginViewModelViewModel::class.java)
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uploadUI.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                when(it[0]){
                    "dashboard" -> {
                        val editor = requireActivity().getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE).edit()
                        editor.putString("gttseguros","1")
                        editor.putString("phone",it[1])
                        editor.apply()
                        startActivity(Intent(requireActivity(), DashboardGttActivity::class.java))
                    }
                    "error" -> {
                        Snackbar.make(view, it[1], Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonBeneficios.setOnClickListener {
            findNavController().navigate(R.id.action_loginViewModelFragment_to_onboardingGttFragment)
        }

        buttonContrata.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                imageView8 to "imageView"
            )
            findNavController().navigate(R.id.action_loginViewModelFragment_to_contractFragment, null, null, extras)
        }

        telefonoInfo.setOnClickListener {
            showAlert(1)
        }

        passwordInfo.setOnClickListener {
            showAlert(2)
        }
    }

    private fun showAlert(type: Int){
        val dialog = BottomSheetDialog(requireContext())
        val view = if ( type == 1 ) layoutInflater.inflate(R.layout.bottom_telefono_info, null) else layoutInflater.inflate(R.layout.bottom_pass_info, null)
        val btnClose = view.findViewById<TextView>(R.id.idBtnDismiss)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }
}