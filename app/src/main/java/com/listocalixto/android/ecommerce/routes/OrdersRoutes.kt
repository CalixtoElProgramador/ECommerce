package com.listocalixto.android.ecommerce.routes

import com.listocalixto.android.ecommerce.models.Order
import com.listocalixto.android.ecommerce.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.*

interface OrdersRoutes {

    @POST("orders/create")
    fun create(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    
}