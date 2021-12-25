package com.listocalixto.android.ecommerce.activities.client.shopping_bag

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.address.list.ClientAddressListActivity
import com.listocalixto.android.ecommerce.adapters.ShoppingBagAdapter
import com.listocalixto.android.ecommerce.models.Product
import com.listocalixto.android.ecommerce.util.SharedPref

class ClientShoppingBagActivity : AppCompatActivity() {

    private val gson = Gson()

    private var sharedPref: SharedPref? = null
    private var adapter: ShoppingBagAdapter? = null
    private var selectedProducts = arrayListOf<Product>()

    private lateinit var rvShoppingBag: RecyclerView
    private lateinit var textPriceTotal: TextView
    private lateinit var btnNext: MaterialButton
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shopping_bag)
        initSharedPref()
        setupView()
        getProductsFromSharedPref()
        setupRecyclerView()
        setupToolbar()

        btnNext.setOnClickListener { navigateToClientAddressListActivity() }

    }

    private fun navigateToClientAddressListActivity() {
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
    }

    private fun initSharedPref() {
        sharedPref = SharedPref(this)
    }

    private fun setupView() {
        rvShoppingBag = findViewById(R.id.rv_shoppingList)
        textPriceTotal = findViewById(R.id.totalPrice)
        btnNext = findViewById(R.id.btn_nextShoppingList)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getData("order").isNullOrBlank()) {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)
        }
    }

    private fun setupRecyclerView() {
        adapter = ShoppingBagAdapter(selectedProducts, this)
        rvShoppingBag.adapter = adapter
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.your_shopping_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("SetTextI18n")
    fun setTotal(total: Double) {
        textPriceTotal.text = "$ $total"
    }

}