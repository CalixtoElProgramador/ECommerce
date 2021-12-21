package com.listocalixto.android.ecommerce.models

import com.google.gson.annotations.SerializedName

data class Role(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("route")
    val route: String
) {

    override fun toString(): String {
        return "Role(id='$id', name='$name', image='$image', route='$route')"
    }
}