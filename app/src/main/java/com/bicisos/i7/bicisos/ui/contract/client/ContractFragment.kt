package com.bicisos.i7.bicisos.ui.contract.client

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.databinding.ContractFragmentBinding
import com.bicisos.i7.bicisos.repository.Repository
import java.util.*

class ContractFragment : Fragment() {

    companion object {
        fun newInstance() =
            ContractFragment()
    }

    private lateinit var viewModel: ContractViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.contract_fragment, container, false)
        val binding : ContractFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.contract_fragment, container, false)
        val repo = Repository()
        val factory =
            ContractViewModelFactory(
                repo,
                requireActivity()
            )

        viewModel = ViewModelProvider(this, factory).get(ContractViewModel::class.java)
        binding.contractViewModel = viewModel
        binding.lifecycleOwner = this

        //requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.uploadUI.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                when(it){
                    "showDatePicker" -> {
                        val c = Calendar.getInstance()
                        val year = c.get(Calendar.YEAR)
                        val month = c.get(Calendar.MONTH)
                        val day = c.get(Calendar.DAY_OF_MONTH)

                        val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                            val month = monthOfYear + 1
                            var fm = "" + month
                            var fd = "" + dayOfMonth
                            if (month < 10) {
                                fm = "0$month"
                            }
                            if (dayOfMonth < 10) {
                                fd = "0$dayOfMonth"
                            }
                            val date = "$fd/$fm/$year"
                            viewModel.setPickerData(date)

                        }, year, month, day)

                        dpd.datePicker.maxDate = c.timeInMillis
                        dpd.show()
                    }
                    else -> {
                        findNavController().navigate(R.id.action_contractFragment_to_photosContractFragment)

                        //message to whatApp
                        //val uri = Uri.parse("smsto:" + "525586140593");
//                        val uri = Uri.parse("https://api.whatsapp.com/send?phone=525511994379&text=" + it)
//                        val intent = Intent(Intent.ACTION_VIEW, uri)
//
//                        if (intent.resolveActivity(requireContext().packageManager) == null) {
//                            Toast.makeText(requireContext(),
//                                "Necesitamos que tenga WhatsApp instalado.",
//                                Toast.LENGTH_SHORT).show()
//                        }else {
//                            // Starting Whatsapp
//                            startActivity(intent)
//                        }
                    }
                }
            }
        })
    }
}
