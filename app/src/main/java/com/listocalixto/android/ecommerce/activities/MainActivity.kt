package com.listocalixto.android.ecommerce.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.ClientActivity
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.UsersProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.isEmailValid
import com.listocalixto.android.ecommerce.util.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val usersProvider = UsersProvider()

    private var imgSignUp: ImageView? = null
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignIn: MaterialButton? = null

    private lateinit var layout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getUserFromSession()
        setupViews()

        btnSignIn?.setOnClickListener { login() }
        imgSignUp?.setOnClickListener { navigateToRegisterActivity() }
    }

    private fun setupViews() {
        imgSignUp = findViewById(R.id.img_singUp)
        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)
        btnSignIn = findViewById(R.id.btn_signIn)
        layout = findViewById(R.id.mainActivityLayout)
    }

    private fun login() {
        val email = inputEmail?.text.toString()
        val password = inputPassword?.text.toString()

        when {
            email.isEmpty() && password.isEmpty() -> {
                showSnackbar(
                    view = layout,
                    snackbarText = R.string.err_empty_fields,
                    timeLength = Snackbar.LENGTH_SHORT,
                    anchorView = btnSignIn,
                    isAnError = true
                )
            }
            !email.isEmailValid() -> {
                showSnackbar(
                    view = layout,
                    snackbarText = R.string.err_invalid_email,
                    timeLength = Snackbar.LENGTH_SHORT,
                    anchorView = btnSignIn,
                    isAnError = true
                )
            }
            else -> {
                authenticateUser(email, password)
            }
        }

    }

    private fun authenticateUser(email: String, password: String) {
        usersProvider.login(email, password)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d(TAG, "onResponse: ${response.body()}")
                if (response.body()?.isSuccess == true) {
                    saveUserInSession(response.body()?.data.toString())
                    navigateToClientActivity()
                } else {
                    showSnackbar(
                        view = layout,
                        snackbarText = R.string.err_incorrect_inputs,
                        timeLength = Snackbar.LENGTH_SHORT,
                        anchorView = btnSignIn,
                        isAnError = true
                    )
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                showSnackbar(
                    view = layout,
                    snackbarText = R.string.err_an_error_was_happened,
                    timeLength = Snackbar.LENGTH_LONG,
                    anchorView = btnSignIn,
                    isAnError = true
                )
            }
        })
    }

    private fun saveUserInSession(data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
    }

    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        if (!sharedPref.getData("user").isNullOrBlank()) {
            navigateToClientActivity()
        }

    }

    private fun navigateToRegisterActivity() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    private fun navigateToClientActivity() {
        val i = Intent(this, ClientActivity::class.java)
        startActivity(i)
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}