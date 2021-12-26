package com.listocalixto.android.ecommerce.activities.client.address.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.ecommerce.R
import java.lang.Exception

class ClientAddressMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var googleMap: GoogleMap? = null
    private var city = ""
    private var country = ""
    private var address = ""
    private var addressLatLng: LatLng? = null

    private lateinit var layout: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var textDirection: TextView
    private lateinit var btnAccept: MaterialButton

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation = locationResult.lastLocation

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_map)
        setupViews()
        setupToolbar()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        btnAccept.setOnClickListener { navigateToCreateAddressActivity() }

    }

    private fun navigateToCreateAddressActivity() {
        val i = Intent()
        i.putExtra("city", city)
        i.putExtra("address", address)
        i.putExtra("country", country)
        i.putExtra("lat", addressLatLng?.latitude)
        i.putExtra("lng", addressLatLng?.longitude)
        setResult(RESULT_OK, i)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun onCamaraMove() {
        googleMap?.setOnCameraIdleListener {
            try {
                val geocoder = Geocoder(this)
                addressLatLng = googleMap?.cameraPosition?.target
                addressLatLng?.let {
                    val addressList = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    city = addressList[0].locality
                    country = addressList[0].countryName
                    address = addressList[0].getAddressLine(0)

                    textDirection.text = "$address $city"
                }

            } catch (e: Exception) {
                Log.e(TAG, "onCamaraMove: ${e.message}", e)
            }
        }
    }

    private fun setupViews() {
        layout = findViewById(R.id.addressMapLayout)
        textDirection = findViewById(R.id.mapDirection)
        btnAccept = findViewById(R.id.btn_acceptLocation)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                    val location = task.result
                    location?.let {
                        googleMap?.moveCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.Builder().target(
                                    LatLng(it.latitude, it.longitude)
                                ).zoom(15f).build()
                            )
                        )
                    } ?: run {
                        requestNewLocationData()
                    }
                }
            } else {
                Snackbar.make(
                    layout,
                    R.string.err_location_permission_is_required,
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.settings) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }.setBackgroundTint(
                    MaterialColors.getColor(this, R.attr.colorError, "Error")
                ).setTextColor(
                    MaterialColors.getColor(this, R.attr.colorOnError, "ErrorText")
                ).setActionTextColor(
                    MaterialColors.getColor(this, R.attr.colorOnError, "Error")
                ).setAnchorView(
                    btnAccept
                ).show()
            }
        } else {
            requestPermission()
        }
    }

    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient?.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()!!
        )

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.address_map_activity_title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapReady(map: GoogleMap)  {
        googleMap = map
        onCamaraMove()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        }
    }

    companion object {
        private const val PERMISSION_ID = 42
        private const val TAG = "ClientAddressMapActivity"
    }

}