package com.listocalixto.android.ecommerce.activities.restaurant.orders.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.adapters.OrderProductsAdapter
import com.listocalixto.android.ecommerce.models.Order
import com.listocalixto.android.ecommerce.models.Product
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.UsersProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RestaurantOrdersDetailActivity : AppCompatActivity() {

    private var usersProvider: UsersProvider? = null
    private var sharedPref: SharedPref? = null
    private var currentUser: User? = null
    private var idDelivery = ""

    private lateinit var layout: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var rvOrderProducts: RecyclerView
    private lateinit var textClientName: TextView
    private lateinit var textDeliverTo: TextView
    private lateinit var textDateOfOrder: TextView
    private lateinit var textStatus: TextView
    private lateinit var textTotalPrice: TextView
    private lateinit var spinner: Spinner
    private lateinit var btnAssignDelivery: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_orders_detail)
        val order = Gson().fromJson(intent.getStringExtra("order"), Order::class.java)
        initSharedPref()
        getUserFromSession()
        initUserProvider()
        setupViews()
        setupSpinnerWitDeliveries()
        setupRecyclerView(order.products)
        setupData(order)
        setupToolbar(order)

    }

    private fun setupSpinnerWitDeliveries() {
        usersProvider?.getDeliveriesMen()?.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                response.body()?.let {
                    val spinnerLayout = android.R.layout.simple_dropdown_item_1line
                    val arrayAdapter =
                        ArrayAdapter(this@RestaurantOrdersDetailActivity, spinnerLayout, it)
                    spinner.adapter = arrayAdapter
                    spinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                adapterView: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                l: Long
                            ) {
                                idDelivery = it[position].id.toString()
                                Log.d(TAG, "onItemSelected: $idDelivery")
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }
                        }
                } ?: run {
                    showSnackbar(
                        layout,
                        R.string.err_missing_permissions,
                        Snackbar.LENGTH_LONG,
                        btnAssignDelivery,
                        true
                    )
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}", t)
                showSnackbar(
                    layout,
                    R.string.err_an_error_was_happened,
                    Snackbar.LENGTH_SHORT,
                    btnAssignDelivery,
                    true
                )
            }
        })
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

    private fun initUserProvider() {
        usersProvider = UsersProvider(currentUser?.sessionToken!!)
    }

    private fun setupRecyclerView(products: ArrayList<Product>) {
        rvOrderProducts.adapter = OrderProductsAdapter(products, this)
    }

    @SuppressLint("SetTextI18n")
    private fun setupData(order: Order) {
        textClientName.text = "${order.client?.name} ${order.client?.lastname}"
        textDeliverTo.text = "${order.address?.address}"
        textDateOfOrder.text = "${order.timestamp}"
        textStatus.text = "${order.status}"
        var totalPrice = 0.0
        order.products.forEach {
            totalPrice += it.price * it.quantity!!
        }
        textTotalPrice.text = "$ $totalPrice"

    }

    private fun setupToolbar(order: Order) {
        toolbar.title = "Orders #${order.id}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViews() {
        layout = findViewById(R.id.restaurantOrdersDetailLayout)
        toolbar = findViewById(R.id.toolbar)
        rvOrderProducts = findViewById(R.id.rv_orderProducts)
        textClientName = findViewById(R.id.clientOrder)
        textDeliverTo = findViewById(R.id.deliverTo)
        textDateOfOrder = findViewById(R.id.dateOfOrder)
        textStatus = findViewById(R.id.statusOrder)
        textTotalPrice = findViewById(R.id.totalPriceOrder)
        spinner = findViewById(R.id.spinner_deliveries)
        btnAssignDelivery = findViewById(R.id.btn_assignDelivery)
    }

    companion object {
        private const val TAG = "RestaurantOrdersDetailActivity"
    }

}