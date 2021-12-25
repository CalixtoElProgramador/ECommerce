package com.listocalixto.android.ecommerce.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.address.create.ClientAddressCreateActivity

class ClientAddressListActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvAddress: RecyclerView
    private lateinit var btnNext: MaterialButton
    private lateinit var btnCreateAnAddress: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)
        setupViews()
        setupToolbar()

        btnCreateAnAddress.setOnClickListener { navigateToClientAddressCreateActivity() }
    }

    private fun navigateToClientAddressCreateActivity() {
        val i = Intent(this, ClientAddressCreateActivity::class.java)
        startActivity(i)
    }

    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        rvAddress = findViewById(R.id.rv_address)
        btnNext = findViewById(R.id.btn_nextAddressList)
        btnCreateAnAddress = findViewById(R.id.btn_createAnAddress)
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.address_list_activity_title)
        toolbar.subtitle = getString(R.string.address_list_activity_subtitle)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}