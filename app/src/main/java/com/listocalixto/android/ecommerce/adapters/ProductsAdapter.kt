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

class ProductsAdapter(private val products: ArrayList<Product>, private val activity: Activity) :
    RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    val sharedPref = SharedPref(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = products[position]
        Glide.with(activity).load(product.image00.toString()).into(holder.imgProduct)
        holder.nameProduct.text = product.name
        holder.priceProduct.text = "$ ${product.price}"

    }

    class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.img_photoProductItem)
        val nameProduct: TextView = view.findViewById(R.id.nameProductItem)
        val priceProduct: TextView = view.findViewById(R.id.priceProductItem)

    }

}