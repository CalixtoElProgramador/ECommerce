package com.listocalixto.android.ecommerce.activities.client.address.create

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.address.map.ClientAddressMapActivity

class ClientAddressCreateActivity : AppCompatActivity() {

    private lateinit var layout: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var inputLayoutDirection: TextInputLayout
    private lateinit var inputLayoutNeighborhood: TextInputLayout
    private lateinit var inputLayoutReferencePoint: TextInputLayout
    private lateinit var inputDirection: TextInputEditText
    private lateinit var inputNeighborhood: TextInputEditText
    private lateinit var inputReferencePoint: TextInputEditText
    private lateinit var btnCreateAddress: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_create)
        setupViews()
        setupToolbar()

        inputReferencePoint.setOnClickListener { navigateToClientAddressMapActivity() }

    }

    private fun navigateToClientAddressMapActivity() {
        val i = Intent(this, ClientAddressMapActivity::class.java)
        startActivity(i)
    }

    private fun setupViews() {
        layout = findViewById(R.id.addressCreateLayout)
        toolbar = findViewById(R.id.toolbar)
        inputLayoutDirection = findViewById(R.id.inputLayout_createDirection)
        inputLayoutNeighborhood = findViewById(R.id.inputLayout_createNeighborhood)
        inputLayoutReferencePoint = findViewById(R.id.inputLayout_createReferencePoint)
        inputDirection = findViewById(R.id.input_createDirection)
        inputNeighborhood = findViewById(R.id.input_createNeighborhood)
        inputReferencePoint = findViewById(R.id.input_createReferencePoint)
        btnCreateAddress = findViewById(R.id.btn_createThisAddress)

    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.address_create_activity_title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}