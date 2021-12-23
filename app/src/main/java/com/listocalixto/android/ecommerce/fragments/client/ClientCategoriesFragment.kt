package com.listocalixto.android.ecommerce.fragments.client

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.adapters.CategoriesAdapter
import com.listocalixto.android.ecommerce.models.Category
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.CategoriesProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientCategoriesFragment : Fragment(R.layout.fragment_client_categories) {

    private var categoriesProvider: CategoriesProvider? = null
    private var adapter: CategoriesAdapter? = null
    private var currentUser: User? = null
    private var sharedPref: SharedPref? = null
    private var categories = arrayListOf<Category>()

    private lateinit var rvCategories: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var layout: FrameLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPref()
        getUserFromSession()
        initCategoriesProvider()
        setupViews(view)
        getCategoriesFromProvider()
        setupToolbar()
    }

    private fun initSharedPref() {
        sharedPref = SharedPref(requireActivity())
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            currentUser = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun initCategoriesProvider() {
        categoriesProvider = CategoriesProvider(currentUser?.sessionToken!!)
    }

    private fun setupViews(view: View) {
        bottomNav = activity?.findViewById(R.id.bottomNav_client)!!
        rvCategories = view.findViewById(R.id.rv_categories)
        toolbar = view.findViewById(R.id.toolbar)
        layout = view.findViewById(R.id.clientCategoriesLayout)
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.categories)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun getCategoriesFromProvider() {
        categoriesProvider?.getAll()?.enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {
                response.body()?.let {
                    categories = it
                    adapter = CategoriesAdapter(categories, requireActivity())
                    rvCategories.adapter = adapter
                } ?: run {
                    showSnackbar(
                        layout,
                        R.string.err_missing_permissions,
                        Snackbar.LENGTH_LONG,
                        bottomNav,
                        true
                    )
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}", t)
                showSnackbar(
                    layout,
                    R.string.err_an_error_was_happened,
                    Snackbar.LENGTH_SHORT,
                    bottomNav,
                    true
                )
            }
        })
    }

    companion object {
        private const val TAG = "ClientCategoriesFragment"
    }

}