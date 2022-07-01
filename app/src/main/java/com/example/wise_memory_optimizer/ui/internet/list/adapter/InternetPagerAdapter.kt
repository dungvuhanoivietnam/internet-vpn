package com.example.wise_memory_optimizer.ui.internet.list.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.wise_memory_optimizer.ui.internet.list.tab.ListInternetFragment

class InternetPagerAdapter(
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = 2

    override fun getPageTitle(position: Int): CharSequence {
        return if (position == 0){
            "Recently"
        }else {
            "Favourite"
        }
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0){
            ListInternetFragment(isFavorite = false)
        }else {
           ListInternetFragment(isFavorite = true)
        }
    }
}