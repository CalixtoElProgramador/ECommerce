package com.listocalixto.android.ecommerce.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id") val id: String? = null,
    @SerializedName("id_client") val idClient: String,
    @SerializedName("id_delivery") val idDelivery: String? = null,
    @SerializedName("id_address") val idAddress: String,
    @SerializedName("status") val status: String? = null,
    @SerializedName("timestamp") var timestamp: Long = 0,
    @SerializedName("products") val products: ArrayList<Product>
) {

    override fun toString(): String {
        return "Order(id=$id, idClient='$idClient', idDelivery=$idDelivery, idAddress='$idAddress', status=$status, timestamp=$timestamp, products=$products)"
    }

    fun toJson(): String = Gson().toJson(this)
}
