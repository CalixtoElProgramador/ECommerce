package com.listocalixto.android.ecommerce.fragments.client

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.MainActivity
import com.listocalixto.android.ecommerce.activities.SelectRolesActivity
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import de.hdodenhof.circleimageview.CircleImageView

class ClientProfileFragment : Fragment(R.layout.fragment_client_profile) {

    private var user: User? = null
    private var sharedPref: SharedPref? = null

    private lateinit var btnEditProfile: ImageView
    private lateinit var btnSelectRole: MaterialButton
    private lateinit var btnLogout: MaterialButton
    private lateinit var imgProfilePicture: CircleImageView
    private lateinit var profileName: TextView
    private lateinit var profilePhone: TextView
    private lateinit var profileEmail: TextView
    private lateinit var layout: CoordinatorLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPref()
        getUserFromSession()
        setupViews(view)
        setupData()

        btnSelectRole.setOnClickListener { navigateToSelectRolesActivity() }
        btnLogout.setOnClickListener { logout() }

    }

    private fun logout() {
        sharedPref?.remove("user")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
        activity?.finish()
    }

    private fun setupData() {
        user?.let {
            profileName.text = it.name
            profilePhone.text = it.phone
            profileEmail.text = it.email
            Glide.with(requireContext()).load(it.image).into(imgProfilePicture)
        } ?: showSnackbar(
            layout,
            R.string.err_an_error_was_happened,
            Snackbar.LENGTH_SHORT,
            btnSelectRole,
            true
        )
    }

    private fun initSharedPref() {
        sharedPref = activity?.let { SharedPref(it) }
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun navigateToSelectRolesActivity() {
        val i = Intent(requireActivity(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        activity?.finish()
    }

    private fun setupViews(view: View) {
        btnEditProfile = view.findViewById(R.id.btn_editProfile)
        btnSelectRole = view.findViewById(R.id.btn_selectRole)
        btnLogout = view.findViewById(R.id.btn_logout)
        imgProfilePicture = view.findViewById(R.id.img_profilePicture)
        profileName = view.findViewById(R.id.profileName)
        profilePhone = view.findViewById(R.id.profileNumber)
        profileEmail = view.findViewById(R.id.profileEmail)
        layout = view.findViewById(R.id.clientProfileLayout)
    }

}