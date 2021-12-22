package com.listocalixto.android.ecommerce.fragments.client

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.SelectRolesActivity

class ClientProfileFragment : Fragment(R.layout.fragment_client_profile) {

    private lateinit var btnSelectRole: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)

        btnSelectRole.setOnClickListener { navigateToSelectRolesActivity() }

    }

    private fun navigateToSelectRolesActivity() {
        val i = Intent(requireActivity(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        activity?.finish()
    }

    private fun setupViews(view: View) {
        btnSelectRole = view.findViewById(R.id.btn_selectRole)
    }

}