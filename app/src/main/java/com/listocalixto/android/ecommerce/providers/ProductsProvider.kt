package com.listocalixto.android.ecommerce.providers

import com.listocalixto.android.ecommerce.api.ApiRoutes
import com.listocalixto.android.ecommerce.models.Category
import com.listocalixto.android.ecommerce.models.Product
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.routes.CategoriesRoutes
import com.listocalixto.android.ecommerce.routes.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider(private val token: String) {

    private var productsRoutes: ProductsRoutes? = null

    init {
        val api = ApiRoutes()
        productsRoutes = api.getProductsRoutes(token)
    }

    fun create(files: List<File>, product: Product): Call<ResponseHttp>? {
        val images = arrayOfNulls<MultipartBody.Part>(files.size)
        for (i in files.indices) {
            val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i])
            images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile)
        }
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), product.toJson())
        return productsRoutes?.create(images, requestBody, token)
    }

}