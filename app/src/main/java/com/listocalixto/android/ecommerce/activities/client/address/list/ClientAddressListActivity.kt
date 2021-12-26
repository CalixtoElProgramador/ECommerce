package com.listocalixto.android.ecommerce.activities.client.address.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.address.create.ClientAddressCreateActivity
import com.listocalixto.android.ecommerce.adapters.AddressAdapter
import com.listocalixto.android.ecommerce.models.Address
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.AddressProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressListActivity : AppCompatActivity() {

    private var sharedPref: SharedPref? = null
    private var currentUser: User? = null
    private var addressProvider: AddressProvider? = null

    private lateinit var layout: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var rvAddress: RecyclerView
    private lateinit var btnNext: MaterialButton
    private lateinit var btnCreateAnAddress: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)
        initSharedPref()
        getUserFromSession()
        initAddressProvider()
        setupViews()
        getAddressesFromProvider()
        setupToolbar()

        btnCreateAnAddress.setOnClickListener { navigateToClientAddressCreateActivity() }
    }

    private fun getAddressesFromProvider() {
        addressProvider?.getAddresses(currentUser?.id!!)
            ?.enqueue(object : Callback<ArrayList<Address>> {
                override fun onResponse(
                    call: Call<ArrayList<Address>>,
                    response: Response<ArrayList<Address>>
                ) {
                    Log.d(TAG, "onResponse: $response")
                    Log.d(TAG, "onResponse: Body - ${response.body()}")
                    response.body()?.let {
                        rvAddress.adapter = AddressAdapter(it, this@ClientAddressListActivity)
                    } ?: run {
                        showSnackbar(
                            layout,
                            R.string.err_internet_connection,
                            Snackbar.LENGTH_LONG,
                            btnNext,
                            true
                        )
                    }
                }

                override fun onFailure(call: Call<ArrayList<Address>>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}", t)
                    showSnackbar(
                        layout,
                        R.string.err_an_error_was_happened,
                        Snackbar.LENGTH_LONG,
                        btnNext,
                        true
                    )
                }
            })
    }

    private fun initSharedPref() {
        sharedPref = SharedPref(this)
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            currentUser = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun initAddressProvider() {
        addressProvider = AddressProvider(currentUser?.sessionToken!!)
    }

    private fun navigateToClientAddressCreateActivity() {
        val i = Intent(this, ClientAddressCreateActivity::class.java)
        startActivity(i)
    }

    private fun setupViews() {
        layout = findViewById(R.id.clientAddressListLayout)
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

    companion object {
        private const val TAG = "ClientAddressListActivity"
    }

}