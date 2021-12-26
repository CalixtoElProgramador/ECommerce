package com.listocalixto.android.ecommerce.activities.client.address.create

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.address.list.ClientAddressListActivity
import com.listocalixto.android.ecommerce.activities.client.address.map.ClientAddressMapActivity
import com.listocalixto.android.ecommerce.models.Address
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.AddressProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressCreateActivity : AppCompatActivity() {

    private var addressLat = 0.0
    private var addressLng = 0.0
    private var addressProvider: AddressProvider? = null
    private var sharedPref: SharedPref? = null
    private var currentUser: User? = null

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
        initSharedPref()
        getUserFromSession()
        initAddressProvider()
        setupViews()
        setupToolbar()

        inputReferencePoint.setOnClickListener { navigateToClientAddressMapActivity() }
        btnCreateAddress.setOnClickListener { validateInputs() }

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

    private fun validateInputs() {
        val direction = inputDirection.text.toString()
        val neighborhood = inputNeighborhood.text.toString()
        val reference = inputReferencePoint.text.toString()
        val idUser = currentUser?.id

        when {
            direction.isEmpty() || neighborhood.isEmpty() || reference.isEmpty() -> {
                showSnackbar(
                    layout,
                    R.string.err_empty_fields,
                    Snackbar.LENGTH_SHORT,
                    btnCreateAddress,
                    true
                )
            }

            idUser.isNullOrEmpty() -> {
                showSnackbar(
                    layout,
                    R.string.err_unidentified_user,
                    Snackbar.LENGTH_SHORT,
                    btnCreateAddress,
                    true
                )
            }

            addressLat == 0.0 || addressLng == 0.0 -> {
                showSnackbar(
                    layout,
                    R.string.err_coordinates_invalids,
                    Snackbar.LENGTH_SHORT,
                    btnCreateAddress,
                    true
                )
            }

            else -> {
                val address = Address(
                    idUser = idUser,
                    address = direction,
                    neighborhood = neighborhood,
                    lat = addressLat,
                    lng = addressLng
                )
                saveAddress(address)
            }
        }
    }

    private fun saveAddress(address: Address) {
        ProgressDialogFragment.showProgressBar(this)
        addressProvider?.create(address)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                ProgressDialogFragment.hideProgressBar(this@ClientAddressCreateActivity)
                Log.d(TAG, "onResponse: $response")
                Log.d(TAG, "onResponse: Body - ${response.body()}")
                response.body()?.let {
                    if (it.isSuccess) {
                        navigateToClientAddressListActivity()
                    } else {
                        showSnackbar(
                            layout,
                            R.string.err_missing_permissions,
                            Snackbar.LENGTH_LONG,
                            btnCreateAddress,
                            true
                        )
                    }
                } ?: run {
                    showSnackbar(
                        layout,
                        R.string.err_internet_connection,
                        Snackbar.LENGTH_LONG,
                        btnCreateAddress,
                        true
                    )
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                ProgressDialogFragment.hideProgressBar(this@ClientAddressCreateActivity)
                Log.e(TAG, "onFailure: ${t.message}", t)
                showSnackbar(
                    layout,
                    R.string.err_an_error_was_happened,
                    Snackbar.LENGTH_LONG,
                    btnCreateAddress,
                    true
                )
            }
        })
    }

    private fun navigateToClientAddressListActivity() {
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun navigateToClientAddressMapActivity() {
        val i = Intent(this, ClientAddressMapActivity::class.java)
        resultLauncher.launch(i)
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

    @SuppressLint("SetTextI18n")
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.let {
                    val city = it.getStringExtra("city")
                    val address = it.getStringExtra("address")
                    val country = it.getStringExtra("country")
                    addressLat = it.getDoubleExtra("lat", 0.0)
                    addressLng = it.getDoubleExtra("lng", 0.0)

                    inputReferencePoint.setText("$address $city")
                }

            }
        }

    companion object {
        private const val TAG = "ClientAddressCreateActivity"
    }

}