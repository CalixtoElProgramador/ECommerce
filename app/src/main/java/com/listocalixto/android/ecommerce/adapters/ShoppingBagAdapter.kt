package com.listocalixto.android.ecommerce.adapters

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

class ShoppingBagAdapter(
    private val products: ArrayList<Product>,
    private val activity: Activity
) :
    RecyclerView.Adapter<ShoppingBagAdapter.ShoppingBagViewHolder>() {

    val sharedPref = SharedPref(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_shopping_bag, parent, false)
        return ShoppingBagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ShoppingBagViewHolder, position: Int) {
        val product = products[position]
        Glide.with(activity).load(product.image00).into(holder.imgProduct)
        holder.nameProduct.text = product.name
        holder.priceProduct.text = product.price.toString()

    }


    class ShoppingBagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.img_productShopItem)
        val nameProduct: TextView = view.findViewById(R.id.nameShopItem)
        val imgAddProduct: ImageView = view.findViewById(R.id.img_addShopItem)
        val quantityProduct: TextView = view.findViewById(R.id.quantityShopItem)
        val imgRemoveProduct: ImageView = view.findViewById(R.id.img_removeShopItem)
        val deleteProduct: ImageView = view.findViewById(R.id.img_deleteShopItem)
        val priceProduct: TextView = view.findViewById(R.id.priceShopItem)

    }

}