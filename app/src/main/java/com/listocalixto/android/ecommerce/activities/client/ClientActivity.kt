package com.listocalixto.android.ecommerce.activities.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.util.SharedPref

class ClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
    }

    companion object {
        private const val TAG = "ClientActivity"
    }

}