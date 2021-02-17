package com.bicisos.i7.bicisos.ui.contract

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.databinding.ContractFragmentBinding
import com.bicisos.i7.bicisos.repository.Repository

class ContractFragment : Fragment() {

    companion object {
        fun newInstance() = ContractFragment()
    }

    private lateinit var viewModel: ContractViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.contract_fragment, container, false)
        val binding : ContractFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.contract_fragment, container, false)
        val repo = Repository()
        val factory = ContractViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory).get(ContractViewModel::class.java)
        binding.contractViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
