package com.listocalixto.android.ecommerce.fragments.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.adapters.DeliveryTabsPagerAdapter

class DeliveryOrdersFragment : Fragment(R.layout.fragment_delivery_orders) {

    private lateinit var tabLayout: TabLayout
    private lateinit var vpOrder: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupViewPagerWithTabLayout()
    }

    private fun setupViewPagerWithTabLayout() {
        val adapter = DeliveryTabsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, 3)
        vpOrder.adapter = adapter
        vpOrder.isUserInputEnabled = true

        TabLayoutMediator(tabLayout, vpOrder) { tab, position ->
            when (position) {
                0 -> { tab.text = "DISPATCHED" }
                1 -> { tab.text = "ON THE WAY" }
                2 -> { tab.text = "DELIVERED" }
            }
        }.attach()
    }

    private fun setupViews(view: View) {
        tabLayout = view.findViewById(R.id.tabLayout_orders)
        vpOrder = view.findViewById(R.id.vp_orders)
    }

}