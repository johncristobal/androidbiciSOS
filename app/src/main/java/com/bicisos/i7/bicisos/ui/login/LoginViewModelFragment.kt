package com.bicisos.i7.bicisos.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bicisos.i7.bicisos.R

class LoginViewModelFragment : Fragment() {

    companion object {
        fun newInstance() = LoginViewModelFragment()
    }

    private lateinit var viewModel: LoginViewModelViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_view_model_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModelViewModel::class.java)
        // TODO: Use the ViewModel
    }

}