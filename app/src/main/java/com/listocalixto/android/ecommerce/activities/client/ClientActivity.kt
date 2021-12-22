package com.listocalixto.android.ecommerce.activities.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.MainActivity
import com.listocalixto.android.ecommerce.fragments.client.ClientCategoriesFragment
import com.listocalixto.android.ecommerce.fragments.client.ClientOrdersFragment
import com.listocalixto.android.ecommerce.fragments.client.ClientProfileFragment
import com.listocalixto.android.ecommerce.util.SharedPref

class ClientActivity : AppCompatActivity() {

    private var sharedPref: SharedPref? = null
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        openFragment(ClientCategoriesFragment())
        initSharedPref()
        setupViews()

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    openFragment(ClientCategoriesFragment())
                    true
                }
                R.id.item_orders -> {
                    openFragment(ClientOrdersFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_client, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    private fun setupViews() {
        bottomNav = findViewById(R.id.bottomNav_client)
    }

    private fun initSharedPref() {
        sharedPref = SharedPref(this)
    }

    private fun logout() {
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    companion object {
        private const val TAG = "ClientActivity"
    }

}