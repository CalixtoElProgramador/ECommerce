package com.listocalixto.android.ecommerce.activities.delivery.orders.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.delivery.orders.map.DeliveryOrdersMapActivity
import com.listocalixto.android.ecommerce.adapters.OrderProductsAdapter
import com.listocalixto.android.ecommerce.models.Order
import com.listocalixto.android.ecommerce.models.Product
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.OrdersProvider
import com.listocalixto.android.ecommerce.providers.UsersProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class DeliveryOrdersDetailActivity : AppCompatActivity() {

    private var ordersProvider: OrdersProvider? = null
    private var usersProvider: UsersProvider? = null
    private var sharedPref: SharedPref? = null
    private var currentUser: User? = null

    private lateinit var layout: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var rvOrderProducts: RecyclerView
    private lateinit var textClientName: TextView
    private lateinit var textDeliverTo: TextView
    private lateinit var textDateOfOrder: TextView
    private lateinit var textStatus: TextView
    private lateinit var textTotalPrice: TextView
    private lateinit var btnStartTrip: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_detail)
        val order = Gson().fromJson(intent.getStringExtra("order"), Order::class.java)
        initSharedPref()
        getUserFromSession()
        initProviders()
        setupViews()
        configureButtonAccordingStatus(order.status, order)
        setupRecyclerView(order.products)
        setupData(order)
        setupToolbar(order)

    }

    private fun configureButtonAccordingStatus(status: String?, order: Order) {
        when(status) {
            "DISPATCHED" -> {
                btnStartTrip.visibility = View.VISIBLE
                btnStartTrip.text = getString(R.string.start_trip)
                btnStartTrip.setOnClickListener { updateOrder(order) }
            }
            "ON THE WAY" -> {
                btnStartTrip.visibility = View.VISIBLE
                btnStartTrip.text = getString(R.string.back_to_the_map)
                btnStartTrip.setOnClickListener { navigateToMap(order) }
            }
            else -> {
                btnStartTrip.visibility = View.GONE
            }
        }
    }

    private fun updateOrder(order: Order) {
        ProgressDialogFragment.showProgressBar(this)
        ordersProvider?.updateToOnTheWay(order)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                ProgressDialogFragment.hideProgressBar(this@DeliveryOrdersDetailActivity)
                response.body()?.let {
                    if (it.isSuccess) {
                        Toast.makeText(
                            this@DeliveryOrdersDetailActivity,
                            R.string.order_started,
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToMap(order)
                    } else {
                        showSnackbar(
                            layout,
                            R.string.err_delivery_can_not_be_assigned,
                            Snackbar.LENGTH_LONG,
                            btnStartTrip,
                            true
                        )
                    }
                } ?: run {
                    showSnackbar(
                        layout,
                        R.string.err_missing_permissions,
                        Snackbar.LENGTH_LONG,
                        btnStartTrip,
                        true
                    )
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                ProgressDialogFragment.hideProgressBar(this@DeliveryOrdersDetailActivity)
                Log.e(TAG, "onFailure: ${t.message}", t)
                showSnackbar(
                    layout,
                    R.string.err_an_error_was_happened,
                    Snackbar.LENGTH_SHORT,
                    btnStartTrip,
                    true
                )
            }
        })
    }

    private fun navigateToMap(order: Order) {
        val i = Intent(this, DeliveryOrdersMapActivity::class.java)
        i.putExtra("order", order.toJson())
        startActivity(i)
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

    private fun initProviders() {
        usersProvider = UsersProvider(currentUser?.sessionToken!!)
        ordersProvider = OrdersProvider(currentUser?.sessionToken!!)
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
        layout = findViewById(R.id.deliveryOrdersDetailLayout)
        toolbar = findViewById(R.id.toolbar)
        rvOrderProducts = findViewById(R.id.rv_orderProducts)
        textClientName = findViewById(R.id.clientOrder)
        textDeliverTo = findViewById(R.id.deliverTo)
        textDateOfOrder = findViewById(R.id.dateOfOrder)
        textStatus = findViewById(R.id.statusOrder)
        textTotalPrice = findViewById(R.id.totalPriceOrder)
        btnStartTrip = findViewById(R.id.btn_assignDelivery)
    }

    companion object {
        private const val TAG = "DeliveryOrdersDetailActivity"
    }

}