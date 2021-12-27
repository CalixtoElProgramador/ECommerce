package com.listocalixto.android.ecommerce.providers

import com.listocalixto.android.ecommerce.api.ApiRoutes
import com.listocalixto.android.ecommerce.models.Category
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.routes.UsersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsersProvider(private val token: String? = null) {

    private var usersRoutes: UsersRoutes? = null
    private var userRoutesWithToken: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUsersRoutes()
        token?.let { userRoutesWithToken = api.getUsersRoutesWithToken(it) }
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

        return userRoutesWithToken?.update(image, requestBody, token!!)

    }

    fun updateWithoutImage(user: User): Call<ResponseHttp>? {
        return userRoutesWithToken?.updateWithoutImage(user, token!!)
    }

    fun getDeliveriesMen(): Call<ArrayList<User>>? = userRoutesWithToken?.getDeliveriesMen(token!!)

}