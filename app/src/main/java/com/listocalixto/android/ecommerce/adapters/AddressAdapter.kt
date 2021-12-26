package com.listocalixto.android.ecommerce.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.address.list.ClientAddressListActivity
import com.listocalixto.android.ecommerce.activities.client.products.list.ClientProductsListActivity
import com.listocalixto.android.ecommerce.models.Address
import com.listocalixto.android.ecommerce.models.Category
import com.listocalixto.android.ecommerce.util.SharedPref

class AddressAdapter(private val addresses: ArrayList<Address>, private val activity: Activity) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    val sharedPref = SharedPref(activity)
    var prev = 0
    var positionAddressSession = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return addresses.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addresses[position]

        if (!sharedPref.getData("address").isNullOrEmpty()) {
            val ad = Gson().fromJson(sharedPref.getData("address"), Address::class.java)
            if (ad.id == address.id) {
                positionAddressSession = position
                holder.imgCheckAddress.visibility = View.VISIBLE
            }
        }

        holder.directionAddress.text = address.address
        holder.neighborhood.text = address.neighborhood
        holder.itemView.setOnClickListener {
            (activity as ClientAddressListActivity).resetValue(prev)
            activity.resetValue(positionAddressSession)
            prev = position
            holder.imgCheckAddress.visibility = View.VISIBLE
            saveAddress(address.toJson())
        }
    }

    private fun saveAddress(data: String) {
        val address = Gson().fromJson(data, Address::class.java)
        sharedPref.save("address", address)
    }

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCheckAddress: ImageView = view.findViewById(R.id.img_checkAddressItem)
        val directionAddress: TextView = view.findViewById(R.id.directionAddressItem)
        val neighborhood: TextView = view.findViewById(R.id.neighborhoodAddressItem)
    }

}