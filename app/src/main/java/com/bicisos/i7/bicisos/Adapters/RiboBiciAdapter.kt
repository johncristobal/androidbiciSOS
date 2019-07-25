package com.bicisos.i7.bicisos.Adapters

//import androidx.core.app.Fragment
//import androidx.core.app.FragmentManager
//import androidx.core.app.FragmentStatePagerAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.bicisos.i7.bicisos.Fragments.RoboBici.RoboDosFragment
import com.bicisos.i7.bicisos.Fragments.RoboBici.RoboTresFragment
import com.bicisos.i7.bicisos.Fragments.RoboBici.RoboUnoFragment

class RiboBiciAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(p0: Int): Fragment? {

        when(p0){
            0 -> return RoboUnoFragment.newInstance("","")
            1 -> return RoboDosFragment.newInstance("","")
            2 -> return RoboTresFragment.newInstance("","")
            else -> return null
        }
    }

    override fun getCount(): Int {

        return 3
    }
}