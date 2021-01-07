package com.bicisos.i7.bicisos.ui.onboarding_gtt

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bicisos.i7.bicisos.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.onboarding_gtt_fragment.*

class OnboardingGttFragment : Fragment() {

    companion object {
        fun newInstance() = OnboardingGttFragment()
    }

    private lateinit var viewModel: OnboardingGttViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.onboarding_gtt_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OnboardingGttViewModel::class.java)
        // TODO: Use the ViewModel

        onboardingViewPager.adapter = OnboardingAdapter(requireActivity(),4)
        TabLayoutMediator(tab_layout, onboardingViewPager) { tab, position ->
            //Some implementation...
        }.attach()
    }

}