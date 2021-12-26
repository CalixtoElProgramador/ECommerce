package com.listocalixto.android.ecommerce.fragments.client

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.adapters.OrdersClientAdapter
import com.listocalixto.android.ecommerce.models.Order
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.OrdersProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientOrdersStatusFragment : Fragment(R.layout.fragment_client_orders_status) {

    private var status = "PAID"
    private var currentUser: User? = null
    private var ordersProvider: OrdersProvider? = null

    private lateinit var sharedPref: SharedPref

    private lateinit var layout: FrameLayout
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var rvOrders: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("status")?.let {
            status = it
        }
        initSharedPref()
        getUserFromSession()
        initOrdersProvider()
        setupViews(view)
        currentUser?.id?.let {
            getOrders(it, status)
        }

    }

    private fun getOrders(idClient: String, status: String) {
        ordersProvider?.getOrdersByClientAndStatus(idClient, status)
            ?.enqueue(object : Callback<ArrayList<Order>> {
                override fun onResponse(
                    call: Call<ArrayList<Order>>,
                    response: Response<ArrayList<Order>>
                ) {
                    Log.d(TAG, "onResponse: $response")
                    Log.d(TAG, "onResponse: Body - ${response.body()}")
                    response.body()?.let {
                        rvOrders.adapter = OrdersClientAdapter(it, requireActivity())
                    } ?: run {
                        showSnackbar(
                            layout,
                            R.string.err_internet_connection,
                            Snackbar.LENGTH_LONG,
                            bottomNav,
                            true
                        )
                    }
                }

                override fun onFailure(call: Call<ArrayList<Order>>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}", t)
                    showSnackbar(
                        layout,
                        R.string.err_an_error_was_happened,
                        Snackbar.LENGTH_LONG,
                        bottomNav,
                        true
                    )
                }
            })
    }

    private fun initOrdersProvider() {
        ordersProvider = OrdersProvider(currentUser?.sessionToken!!)
    }

    private fun setupViews(view: View) {
        layout = view.findViewById(R.id.clientOrdersStatus)
        bottomNav = requireActivity().findViewById(R.id.bottomNav_client)
        rvOrders = view.findViewById(R.id.rv_orders)
    }

    private fun initSharedPref() {
        sharedPref = SharedPref(requireActivity())
    }

    private fun getUserFromSession() {
        if (!sharedPref.getData("user").isNullOrBlank()) {
            currentUser = Gson().fromJson(sharedPref.getData("user"), User::class.java)
        }
    }

    companion object {
        private const val TAG = "ClientOrdersStatusFragment"
    }


}