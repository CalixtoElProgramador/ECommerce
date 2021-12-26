package com.listocalixto.android.ecommerce.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.listocalixto.android.ecommerce.fragments.client.ClientOrdersStatusFragment

class TabsPagerAdapter(
    fragmentManager: FragmentManager,
    lifeCycle: Lifecycle,
    var numberOfTabs: Int
) : FragmentStateAdapter(fragmentManager, lifeCycle) {

    override fun getItemCount(): Int {
        return numberOfTabs
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString("status", "PAID")
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "DISPATCHED")
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status", "ON THE WAY")
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString("status", "DELIVERED")
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            else -> return ClientOrdersStatusFragment()
        }
    }
}