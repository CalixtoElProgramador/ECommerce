package com.listocalixto.android.ecommerce.providers

import com.listocalixto.android.ecommerce.api.ApiRoutes
import com.listocalixto.android.ecommerce.models.Order
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.routes.OrdersRoutes
import retrofit2.Call

class OrdersProvider(private val token: String) {

    private var orderRoutes: OrdersRoutes? = null

    init {
        val api = ApiRoutes()
        orderRoutes = api.getOrderRoutes(token)
    }

    fun create(order: Order): Call<ResponseHttp>? = orderRoutes?.create(order, token)

}