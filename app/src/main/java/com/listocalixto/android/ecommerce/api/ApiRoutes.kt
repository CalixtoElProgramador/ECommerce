package com.listocalixto.android.ecommerce.api

import com.listocalixto.android.ecommerce.routes.*

class ApiRoutes {

    private val retrofit = RetrofitClient()

    fun getUsersRoutes(): UsersRoutes {
        return retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }

    fun getUsersRoutesWithToken(token: String): UsersRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(UsersRoutes::class.java)
    }

    fun getCategoriesRoutes(token: String): CategoriesRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(CategoriesRoutes::class.java)
    }

    fun getProductsRoutes(token: String): ProductsRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(ProductsRoutes::class.java)
    }

    fun getAddressRoutes(token: String): AddressRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(AddressRoutes::class.java)
    }

    fun getOrderRoutes(token: String): OrdersRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(OrdersRoutes::class.java)
    }

    companion object {
        private const val API_URL = "http://192.168.1.70:3000/api/"
    }

}