package com.listocalixto.android.ecommerce.providers

import com.listocalixto.android.ecommerce.api.ApiRoutes
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.routes.UsersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsersProvider {

    private var usersRoutes: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUsersRoutes()
    }

    fun register(user: User): Call<ResponseHttp>? {
        return usersRoutes?.register(user)
    }

    fun login(email: String, password: String): Call<ResponseHttp>? {
        return usersRoutes?.login(email, password)
    }

    fun update(file: File, user: User): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), user.toJson())

        return usersRoutes?.update(image, requestBody)

    }

    fun updateWithoutImage(user: User): Call<ResponseHttp>? {
        return usersRoutes?.updateWithoutImage(user)
    }

}