package com.listocalixto.android.ecommerce.activities.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.adapters.ProductsAdapter
import com.listocalixto.android.ecommerce.models.Product
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.ProductsProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity : AppCompatActivity() {

    private var currentUser: User? = null
    private var sharedPref: SharedPref? = null
    private var productsProvider: ProductsProvider? = null

    private lateinit var layout: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var rvProducts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)
        initSharedPref()
        getUserFromSession()
        initProductsProvider()
        setupViews()
        val idCategory = intent.getStringExtra("idCategory")
        getProducts(idCategory.toString())
        setupToolbar()
    }

    private fun initSharedPref() {
        sharedPref = SharedPref(this)
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            currentUser = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun initProductsProvider() {
        productsProvider = ProductsProvider(currentUser?.sessionToken!!)
    }

    private fun setupViews() {
        layout = findViewById(R.id.clientProductsListLayout)
        rvProducts = findViewById(R.id.rv_products)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun getProducts(idCategory: String) {
        productsProvider?.findByCategory(idCategory)
            ?.enqueue(object : Callback<ArrayList<Product>> {
                override fun onResponse(
                    call: Call<ArrayList<Product>>,
                    response: Response<ArrayList<Product>>
                ) {
                    Log.d(TAG, "onResponse: $response")
                    Log.d(TAG, "onResponse: Body - ${response.body()}")
                    response.body()?.let {
                        rvProducts.adapter = ProductsAdapter(it, this@ClientProductsListActivity)
                    } ?: run {
                        showSnackbar(
                            layout,
                            R.string.err_missing_permissions,
                            Snackbar.LENGTH_LONG,
                            layout,
                            true
                        )
                    }
                }

                override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}", t)
                    showSnackbar(
                        layout,
                        R.string.err_an_error_was_happened,
                        Snackbar.LENGTH_SHORT,
                        layout,
                        true
                    )
                }
            })
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.products)
        setSupportActionBar(toolbar)
    }

    companion object {
        private const val TAG = "ClientProductListActivity"
    }

}