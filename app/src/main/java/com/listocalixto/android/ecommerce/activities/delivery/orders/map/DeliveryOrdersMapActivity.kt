package com.listocalixto.android.ecommerce.activities.delivery.orders.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.models.Order
import com.maps.route.extensions.drawRouteOnMap

class DeliveryOrdersMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var googleMap: GoogleMap? = null
    private var city = ""
    private var country = ""
    private var address = ""
    private var addressLatLng: LatLng? = null
    private var myLocationLatLng: LatLng? = null
    private var order: Order? = null

    private var markerDelivery: Marker? = null
    private var markerAddress: Marker? = null

    private lateinit var layout: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var btnAccept: MaterialButton

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            myLocationLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            deleteDeliveryMarker()
            addDeliveryMarker()
            addAddressMarker()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_map)
        order = Gson().fromJson(intent.getStringExtra("order"), Order::class.java)
        setupViews()
        setupToolbar()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        btnAccept.setOnClickListener { navigateToCreateAddressActivity() }

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true
    }

    private fun deleteDeliveryMarker() {
        markerDelivery?.remove()
    }

    private fun drawRoute() {
        val addressLocation = LatLng(order?.address?.lat!!, order?.address?.lng!!)
        googleMap?.drawRouteOnMap(
            getString(R.string.google_maps_key),
            source = myLocationLatLng!!,
            destination = addressLocation,
            context = this,
            color = Color.GREEN,
            polygonWidth = 8,
            markers = false
        )
    }

    private fun addDeliveryMarker() {
        markerDelivery = googleMap?.addMarker(
            MarkerOptions().position(myLocationLatLng!!).title("My position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_bike_man))
        )
    }

    private fun addAddressMarker() {
        val addressLocation = LatLng(order?.address?.lat!!, order?.address?.lng!!)
        markerAddress = googleMap?.addMarker(
            MarkerOptions().position(addressLocation).title("Delivery here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_client))
        )
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

    private fun setupViews() {
        layout = findViewById(R.id.deliveryOrdersMapLayout)
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
                requestNewLocationData()
                fusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                    val location = task.result
                    myLocationLatLng = LatLng(location.latitude, location.longitude)

                    deleteDeliveryMarker()
                    addDeliveryMarker()
                    addAddressMarker()
                    drawRoute()

                    googleMap?.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder().target(
                                LatLng(myLocationLatLng?.latitude!!, myLocationLatLng?.longitude!!)
                            ).zoom(15f).build()
                        )
                    )

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
        private const val TAG = "DeliveryOrdersMapActivity"
    }

}