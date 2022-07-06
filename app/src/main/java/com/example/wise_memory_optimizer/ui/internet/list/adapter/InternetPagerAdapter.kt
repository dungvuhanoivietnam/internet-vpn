package com.example.wise_memory_optimizer.ui.internet.list.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wise_memory_optimizer.ui.internet.list.tab.FavoriteInternetFragment
import com.example.wise_memory_optimizer.ui.internet.list.tab.RecentlyInternetFragment

private const val NUM_TABS = 2

class InternetPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm,lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            RecentlyInternetFragment()
        }else {
            FavoriteInternetFragment()
        }
    }
}