package com.listocalixto.android.ecommerce.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.models.Product
import com.listocalixto.android.ecommerce.util.SharedPref

class OrderProductsAdapter(
    private val products: ArrayList<Product>,
    private val activity: Activity
) :
    RecyclerView.Adapter<OrderProductsAdapter.OrderProductsViewHolder>() {

    val sharedPref = SharedPref(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_order_products, parent, false)
        return OrderProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderProductsViewHolder, position: Int) {
        val product = products[position]
        Glide.with(activity).load(product.image00).into(holder.imgProduct)
        holder.nameProduct.text = product.name
        product.quantity?.let {
            holder.quantityProduct.text = "$it"
        }
    }

    class OrderProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.img_orderProductItem)
        val nameProduct: TextView = view.findViewById(R.id.nameOrderProductItem)
        val quantityProduct: TextView = view.findViewById(R.id.quantityOrderProductItem)
    }

}