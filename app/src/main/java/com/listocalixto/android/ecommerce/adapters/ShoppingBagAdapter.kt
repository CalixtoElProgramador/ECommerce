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
import com.listocalixto.android.ecommerce.activities.client.shopping_bag.ClientShoppingBagActivity
import com.listocalixto.android.ecommerce.models.Product
import com.listocalixto.android.ecommerce.util.SharedPref

class ShoppingBagAdapter(
    private val products: ArrayList<Product>,
    private val activity: Activity
) :
    RecyclerView.Adapter<ShoppingBagAdapter.ShoppingBagViewHolder>() {

    val sharedPref = SharedPref(activity)

    init {
        (activity as ClientShoppingBagActivity).setTotal(getTotal())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_shopping_bag, parent, false)
        return ShoppingBagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShoppingBagViewHolder, position: Int) {
        val product = products[position]
        Glide.with(activity).load(product.image00).into(holder.imgProduct)
        holder.nameProduct.text = product.name

        if (product.quantity != null) {
            holder.priceProduct.text = "$ ${product.price * product.quantity!!}"
        }

        holder.quantityProduct.text = product.quantity.toString()

        holder.imgAddProduct.setOnClickListener { addItem(product, holder) }
        holder.imgRemoveProduct.setOnClickListener { removeItem(product, holder) }
        holder.deleteProduct.setOnClickListener { deleteItem(position) }

    }

    private fun getIndexOf(idProduct: String): Int {
        var pos = 0
        products.forEach {
            if (it.id == idProduct) {
                return pos
            }
            pos++
        }

        return -1
    }

    @SuppressLint("SetTextI18n")
    private fun addItem(product: Product, holder: ShoppingBagViewHolder) {
        val index = getIndexOf(product.id!!)
        product.quantity = product.quantity!! + 1
        products[index].quantity = product.quantity
        holder.quantityProduct.text = "${product.quantity}"
        holder.priceProduct.text = "$ ${product.price * product.quantity!!}"
        sharedPref.save("order", products)
        (activity as ClientShoppingBagActivity).setTotal(getTotal())
    }

    @SuppressLint("SetTextI18n")
    private fun removeItem(product: Product, holder: ShoppingBagViewHolder) {
        if (product.quantity!! > 1) {
            val index = getIndexOf(product.id!!)
            product.quantity = product.quantity!! - 1
            products[index].quantity = product.quantity
            holder.quantityProduct.text = "${product.quantity}"
            holder.priceProduct.text = "$ ${product.price * product.quantity!!}"
            sharedPref.save("order", products)
            (activity as ClientShoppingBagActivity).setTotal(getTotal())
        }
    }

    private fun deleteItem(position: Int) {
        products.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, products.size)
        sharedPref.save("order", products)
    }


    private fun getTotal(): Double {
        var total = 0.0
        products.forEach {
            if (it.quantity != null) {
                total += (it.quantity!! * it.price)
            }
        }
        return total
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