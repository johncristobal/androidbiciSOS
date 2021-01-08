package com.bicisos.i7.bicisos.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.databinding.LoginViewModelFragmentBinding
import com.bicisos.i7.bicisos.repository.Repository
import kotlinx.android.synthetic.main.login_view_model_fragment.*

class LoginViewModelFragment : Fragment() {

    companion object {
        fun newInstance() = LoginViewModelFragment()
    }

    private lateinit var viewModel: LoginViewModelViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.login_view_model_fragment, container, false)
        val binding : LoginViewModelFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.login_view_model_fragment, container, false)
        val repo = Repository()
        val factory = LoginViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory).get(LoginViewModelViewModel::class.java)
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(LoginViewModelViewModel::class.java)
        // TODO: Use the ViewModel

//        viewModel.launch.observe(viewLifecycleOwner, Observer<String> { data ->
//            when(data){
//                "onboarding" -> {
//                    findNavController().navigate(R.id.action_loginViewModelFragment_to_onboardingGttFragment)
//                }
//                "contrata" -> {
//                    findNavController().navigate(R.id.action_loginViewModelFragment_to_contractFragment)
//                }
//                else -> {
//
//                }
//            }
//        })

        buttonBeneficios.setOnClickListener {
            findNavController().navigate(R.id.action_loginViewModelFragment_to_onboardingGttFragment)
        }

        buttonContrata.setOnClickListener {
            findNavController().navigate(R.id.action_loginViewModelFragment_to_contractFragment)
        }
    }
}