package com.bicisos.i7.bicisos.ui.onboarding_gtt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bicisos.i7.bicisos.R
import kotlinx.android.synthetic.main.fragment_item_onboarding.*

private const val ARG_PARAM = "position"

class ItemOnboardingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_PARAM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_item_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayTitles = resources.getStringArray(R.array.onboarding_titles)
        val title = arrayTitles[position!!]
        titleOnboarding.text = title

        val arrayDes = resources.getStringArray(R.array.onboarding_descriptions)
        val description = arrayDes[position!!]
        descriptionOnboarding.text = description

        val arrayImages = resources.obtainTypedArray(R.array.onboarding_imgs)
        val imag = arrayImages.getResourceId(position!!,0)
        imageViewOnboarding.setImageResource(imag)

        arrayImages.recycle()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Int) =
            ItemOnboardingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM, param1)
                }
            }
    }
}