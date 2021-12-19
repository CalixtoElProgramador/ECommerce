package com.listocalixto.android.ecommerce.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.listocalixto.android.ecommerce.R

class MainActivity : AppCompatActivity() {

    private var imgSignUp: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgSignUp = findViewById(R.id.img_singUp)
        imgSignUp?.setOnClickListener { navigateToRegisterActivity() }

    }

    private fun navigateToRegisterActivity() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }
}