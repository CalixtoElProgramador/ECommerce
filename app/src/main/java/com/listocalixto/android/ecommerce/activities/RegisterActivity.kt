package com.listocalixto.android.ecommerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.util.isEmailValid
import com.listocalixto.android.ecommerce.util.showSnackbar

class RegisterActivity : AppCompatActivity() {

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
                showSnackbar(
                    view = layout,
                    snackbarText = R.string.app_name,
                    timeLength = Snackbar.LENGTH_SHORT,
                    anchorView = btnRegister,
                    isAnError = false
                )
            }
        }

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