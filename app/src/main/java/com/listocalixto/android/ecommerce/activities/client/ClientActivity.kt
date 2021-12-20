package com.listocalixto.android.ecommerce.activities.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.MainActivity
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.util.SharedPref

class ClientActivity : AppCompatActivity() {

    private var btnLogout: MaterialButton? = null
    private var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        sharedPref = SharedPref(this)
        setupViews()

        btnLogout?.setOnClickListener { logout() }
    }

    private fun logout() {
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun setupViews() {
        btnLogout = findViewById(R.id.btn_logout)
    }

    companion object {
        private const val TAG = "ClientActivity"
    }

}