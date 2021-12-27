package com.listocalixto.android.ecommerce.routes

import com.listocalixto.android.ecommerce.models.Order
import com.listocalixto.android.ecommerce.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.*

interface OrdersRoutes {

    @GET("orders/findByClientAndStatus/{id_client}/{status}")
    fun getOrdersByClientAndStatus(
        @Path("id_client") idClient: String,
        @Path("status") status: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Order>>

    @GET("orders/findByStatus/{status}")
    fun getOrdersByStatus(
        @Path("status") status: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Order>>

    @POST("orders/create")
    fun create(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("orders/updateToDispatched")
    fun updateToDispatched(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @POST("orders/updateToOnTheWay")
    fun updateToOnTheWay(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @POST("orders/updateToDelivered")
    fun updateToDelivered(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    
}