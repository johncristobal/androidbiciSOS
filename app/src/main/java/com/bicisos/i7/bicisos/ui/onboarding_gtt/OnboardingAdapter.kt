package com.bicisos.i7.bicisos.ui.onboarding_gtt

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//https://stackoverflow.com/questions/6945678/storing-r-drawable-ids-in-xml-array
//https://androidwave.com/viewpager2-with-fragments-android-example/

class OnboardingAdapter (activity: FragmentActivity, val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return ItemOnboardingFragment.newInstance("","")
    }
}