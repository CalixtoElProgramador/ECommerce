package com.listocalixto.android.ecommerce.routes

import com.listocalixto.android.ecommerce.models.Address
import com.listocalixto.android.ecommerce.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.*

interface AddressRoutes {

    @GET("address/findByUser/{id_user}")
    fun getAddresses(
        @Path("id_user") idUser: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Address>>

    @POST("address/create")
    fun create(
        @Body address: Address,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    
}