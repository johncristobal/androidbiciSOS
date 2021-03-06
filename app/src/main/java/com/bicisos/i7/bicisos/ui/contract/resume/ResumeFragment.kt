package com.bicisos.i7.bicisos.ui.contract.resume

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bicisos.i7.bicisos.Api.ServiceApi
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

        val repo = Repository(ServiceApi())
        val factory = ResumeViewModelFactory(repo, requireActivity())

        viewModel = ViewModelProvider(this, factory).get(ResumeViewModel::class.java)
        binding.viewModelResume = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.uploadUI.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                Log.w("data", it)
                when(it){
                    "cerrar" -> {
                        showAlert("Listo","Hemos enviado la información, pronto te daremos mas noticias de tu contratación.")
                    }
                    else -> {
                        showAlert("Atención",it)
                    }
                }
            }
        })
    }

    fun showAlert(title: String, msg: String){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(msg)
        alertDialogBuilder.setPositiveButton("Continuar") { _, _ ->
            requireActivity().finishAndRemoveTask()
        }

        alertDialogBuilder.create().show()
    }
}
