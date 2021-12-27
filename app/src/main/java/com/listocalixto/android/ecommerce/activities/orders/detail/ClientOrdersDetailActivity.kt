package com.listocalixto.android.ecommerce.activities.orders.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.adapters.OrderProductsAdapter
import com.listocalixto.android.ecommerce.models.Order
import com.listocalixto.android.ecommerce.models.Product
import java.util.ArrayList

class ClientOrdersDetailActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvOrderProducts: RecyclerView
    private lateinit var textClientName: TextView
    private lateinit var textDeliverTo: TextView
    private lateinit var textDateOfOrder: TextView
    private lateinit var textStatus: TextView
    private lateinit var textTotalPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_orders_detail)
        val order = Gson().fromJson(intent.getStringExtra("order"), Order::class.java)
        setupViews()
        setupRecyclerView(order.products)
        setupData(order)
        setupToolbar(order)

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
        toolbar = findViewById(R.id.toolbar)
        rvOrderProducts = findViewById(R.id.rv_orderProducts)
        textClientName = findViewById(R.id.clientOrder)
        textDeliverTo = findViewById(R.id.deliverTo)
        textDateOfOrder = findViewById(R.id.dateOfOrder)
        textStatus = findViewById(R.id.statusOrder)
        textTotalPrice = findViewById(R.id.totalPriceOrder)
    }

    companion object {
        private const val TAG = "ClientOrdersDetailActivity"
    }

}