package com.listocalixto.android.ecommerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.adapters.RolesAdapter
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.util.SharedPref

class SelectRolesActivity : AppCompatActivity() {

    private var user: User? = null
    private var rvRoles: RecyclerView? = null
    private var adapter: RolesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)
        getUserFromSession()
        setupViews()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = RolesAdapter(user?.roles!!, this)
        rvRoles?.let {
            it.adapter = adapter
        }
    }

    private fun setupViews() {
        rvRoles = findViewById(R.id.rv_roles)
    }

    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        if (!sharedPref.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }

    }

}