package com.listocalixto.android.ecommerce.routes

import com.listocalixto.android.ecommerce.models.Address
import com.listocalixto.android.ecommerce.models.Category
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AddressRoutes {

    @POST("address/create")
    fun create(
        @Body address: Address,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    
}