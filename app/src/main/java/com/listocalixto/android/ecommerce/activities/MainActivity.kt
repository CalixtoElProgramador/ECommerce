package com.listocalixto.android.ecommerce.activities

import android.content.Intent
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

class MainActivity : AppCompatActivity() {

    private var imgSignUp: ImageView? = null
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignIn: MaterialButton? = null

    private lateinit var layout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                showSnackbar(
                    view = layout,
                    snackbarText = R.string.app_name,
                    timeLength = Snackbar.LENGTH_SHORT,
                    anchorView = btnSignIn,
                    isAnError = false
                )
            }
        }

    }

    private fun navigateToRegisterActivity() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}