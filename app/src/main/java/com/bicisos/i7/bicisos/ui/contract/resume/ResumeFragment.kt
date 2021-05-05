package com.bicisos.i7.bicisos.ui.contract.resume

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.databinding.FragmentResumeBinding
import com.bicisos.i7.bicisos.repository.Repository

class ResumeFragment : Fragment() {

    companion object {
        fun newInstance() = ResumeFragment()
    }

    private lateinit var viewModel: ResumeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentResumeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_resume,
            container,
            false
        )

        val repo = Repository()
        val factory = ResumeViewModelFactory(repo, requireActivity())

        viewModel = ViewModelProvider(this, factory).get(ResumeViewModel::class.java)
        binding.viewModelResume = viewModel
        binding.lifecycleOwner = this

        return binding.root
        //return inflater.inflate(R.layout.fragment_resume, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}