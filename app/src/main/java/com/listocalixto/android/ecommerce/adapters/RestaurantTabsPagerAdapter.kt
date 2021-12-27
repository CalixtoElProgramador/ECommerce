package com.listocalixto.android.ecommerce.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.listocalixto.android.ecommerce.fragments.restaurant.RestaurantOrdersStatusFragment

class RestaurantTabsPagerAdapter(
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
                val fragment = RestaurantOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "DISPATCHED")
                val fragment = RestaurantOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status", "ON THE WAY")
                val fragment = RestaurantOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString("status", "DELIVERED")
                val fragment = RestaurantOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            else -> return RestaurantOrdersStatusFragment()
        }
    }
}