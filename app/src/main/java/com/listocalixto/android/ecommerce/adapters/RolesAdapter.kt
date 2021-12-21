package com.listocalixto.android.ecommerce.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.models.Role

class RolesAdapter(val roles: ArrayList<Role>, val activity: Activity) :
    RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)
        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val role = roles[position]
        holder.textViewRole.text = role.name
        Glide.with(activity).load(role.image).into(holder.imageViewRole)
    }

    class RolesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewRole: TextView = view.findViewById(R.id.roleNameItem)
        val imageViewRole: ImageView = view.findViewById(R.id.img_roleIconItem)

    }

}