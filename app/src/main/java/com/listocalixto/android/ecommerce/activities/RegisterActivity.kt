package com.listocalixto.android.ecommerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.listocalixto.android.ecommerce.R

class RegisterActivity : AppCompatActivity() {

    private var imgBackToLogin: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imgBackToLogin = findViewById(R.id.img_backToLogin)
        imgBackToLogin?.setOnClickListener { navigateToLoginActivity() }

    }

    private fun navigateToLoginActivity() {
        onBackPressed()
    }
}