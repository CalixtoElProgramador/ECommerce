package com.listocalixto.android.ecommerce.providers

import com.listocalixto.android.ecommerce.api.ApiRoutes
import com.listocalixto.android.ecommerce.models.Address
import com.listocalixto.android.ecommerce.models.Category
import com.listocalixto.android.ecommerce.models.Product
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.routes.AddressRoutes
import com.listocalixto.android.ecommerce.routes.CategoriesRoutes
import retrofit2.Call
import java.io.File

class AddressProvider(private val token: String) {

    private var addressRoutes: AddressRoutes? = null

    init {
        val api = ApiRoutes()
        addressRoutes = api.getAddressRoutes(token)
    }

    fun create(address: Address): Call<ResponseHttp>? = addressRoutes?.create(address, token)

    fun getAddresses(idUser: String): Call<ArrayList<Address>>? =
        addressRoutes?.getAddresses(idUser, token)

}