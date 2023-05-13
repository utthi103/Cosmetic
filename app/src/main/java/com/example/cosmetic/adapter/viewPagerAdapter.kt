package com.example.cosmetic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cosmetic.Fr_cart
import com.example.cosmetic.Fr_home
import com.example.cosmetic.Fr_user

class viewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = listOf(Fr_home(), Fr_cart(), Fr_user())

    override fun getCount() = fragmentList.size

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }
}