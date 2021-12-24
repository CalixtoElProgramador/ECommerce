package com.listocalixto.android.ecommerce.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image00") val image00: String? = null,
    @SerializedName("image01") val image01: String? = null,
    @SerializedName("image02") val image02: String? = null,
    @SerializedName("id_category") val idCategory: String,
    @SerializedName("quantity") val quantity: Int? = null
) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Product(id='$id', name='$name', description='$description', price=$price, image00='$image00', image01='$image01', image02='$image02', idCategory='$idCategory', quantity=$quantity)"
    }


}