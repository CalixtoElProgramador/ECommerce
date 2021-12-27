package com.listocalixto.android.ecommerce.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.orders.detail.ClientOrdersDetailActivity
import com.listocalixto.android.ecommerce.models.Order
import com.listocalixto.android.ecommerce.util.SharedPref

class OrdersClientAdapter(private val orders: ArrayList<Order>, private val activity: Activity) :
    RecyclerView.Adapter<OrdersClientAdapter.OrdersViewHolder>() {

    val sharedPref = SharedPref(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders, parent, false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = orders[position]
        holder.orderId.text = "Order #${order.id}"
        holder.dateOfOrder.text = order.timestamp.toString()
        holder.deliverTo.text = order.address?.address

        holder.itemView.setOnClickListener { navigateToOrdersDetailActivity(order) }
    }

    private fun navigateToOrdersDetailActivity(order: Order) {
        val i = Intent(activity, ClientOrdersDetailActivity::class.java)
        i.putExtra("order", order.toJson())
        activity.startActivity(i)
    }

    class OrdersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: TextView = view.findViewById(R.id.orderId)
        val dateOfOrder: TextView = view.findViewById(R.id.dateOfOrderItem)
        val deliverTo: TextView = view.findViewById(R.id.deliverToItem)
    }

}