package com.listocalixto.android.ecommerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.UsersProvider
import com.listocalixto.android.ecommerce.util.isEmailValid
import com.listocalixto.android.ecommerce.util.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private val userProvider = UsersProvider()

    private var imgBackToLogin: ImageView? = null
    private var inputName: EditText? = null
    private var inputLastname: EditText? = null
    private var inputEmail: EditText? = null
    private var inputPhone: EditText? = null
    private var inputPass: EditText? = null
    private var inputConfirmPass: EditText? = null
    private var btnRegister: MaterialButton? = null

    private lateinit var layout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupViews()

        btnRegister?.setOnClickListener { validateInputs() }
        imgBackToLogin?.setOnClickListener { navigateToLoginActivity() }

    }

    private fun validateInputs() {
        val name = inputName?.text.toString()
        val lastname = inputLastname?.text.toString()
        val email = inputEmail?.text.toString()
        val phone = inputPhone?.text.toString()
        val pass = inputPass?.text.toString()
        val confirmPass = inputConfirmPass?.text.toString()

        when {
            name.isEmpty() && lastname.isEmpty() && email.isEmpty() && phone.isEmpty() && pass.isEmpty() && confirmPass.isEmpty() -> {
                showSnackbar(
                    view = layout,
                    snackbarText = R.string.err_empty_fields,
                    timeLength = Snackbar.LENGTH_SHORT,
                    anchorView = btnRegister,
                    isAnError = true
                )
            }
            !email.isEmailValid() -> {
                showSnackbar(
                    view = layout,
                    snackbarText = R.string.err_invalid_email,
                    timeLength = Snackbar.LENGTH_SHORT,
                    anchorView = btnRegister,
                    isAnError = true
                )
            }
            pass != confirmPass -> {
                showSnackbar(
                    view = layout,
                    snackbarText = R.string.err_different_passwords,
                    timeLength = Snackbar.LENGTH_SHORT,
                    anchorView = btnRegister,
                    isAnError = true
                )
            }
            else -> {
                val user = User(
                    name = name,
                    lastname = lastname,
                    email = email,
                    phone = phone,
                    password = pass
                )
                registerUser(user)
            }
        }

    }

    private fun registerUser(user: User) {
        userProvider.register(user)?.enqueue(object: Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d(TAG, "onResponse: $response")
                Log.d(TAG, "body: ${response.body()}")

                showSnackbar(
                    view = layout,
                    snackbarText = R.string.app_name,
                    timeLength = Snackbar.LENGTH_SHORT,
                    anchorView = btnRegister,
                    isAnError = false
                )
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Log.d(TAG, "onFailure: An error was happen: ${t.message}")
                showSnackbar(
                    view = layout,
                    snackbarText = R.string.err_register_new_user,
                    timeLength = Snackbar.LENGTH_SHORT,
                    anchorView = btnRegister,
                    isAnError = true
                )

            }
        })
    }

    private fun setupViews() {
        imgBackToLogin = findViewById(R.id.img_backToLogin)
        inputName = findViewById(R.id.input_name)
        inputLastname = findViewById(R.id.input_lastname)
        inputEmail = findViewById(R.id.input_registerEmail)
        inputPhone = findViewById(R.id.input_registerPhone)
        inputPass = findViewById(R.id.input_registerPassword)
        inputConfirmPass = findViewById(R.id.input_registerConfirmPassword)
        btnRegister = findViewById(R.id.btn_register)
        layout = findViewById(R.id.registerActivityLayout)
    }

    private fun navigateToLoginActivity() {
        onBackPressed()
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }

}