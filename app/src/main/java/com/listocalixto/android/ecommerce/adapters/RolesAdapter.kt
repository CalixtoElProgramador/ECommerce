package com.listocalixto.android.ecommerce.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.ClientActivity
import com.listocalixto.android.ecommerce.activities.delivery.DeliveryHomeActivity
import com.listocalixto.android.ecommerce.activities.restaurant.RestaurantHomeActivity
import com.listocalixto.android.ecommerce.models.Role
import com.listocalixto.android.ecommerce.util.SharedPref

class RolesAdapter(private val roles: ArrayList<Role>, private val activity: Activity) :
    RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    val sharedPref = SharedPref(activity)

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

        holder.itemView.setOnClickListener { navigateToRole(role) }

    }

    private fun navigateToRole(role: Role) {
        if (role.name == "RESTAURANT") {
            sharedPref.save("role", "RESTAURANT")
            val i = Intent(activity, RestaurantHomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(i)
            return
        }

        if (role.name == "DELIVERY") {
            sharedPref.save("role", "DELIVERY")
            val i = Intent(activity, DeliveryHomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(i)
            return
        }

        if (role.name == "CLIENT") {
            sharedPref.save("role", "CLIENT")
            val i = Intent(activity, ClientActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(i)
            return
        }
    }

    class RolesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewRole: TextView = view.findViewById(R.id.roleNameItem)
        val imageViewRole: ImageView = view.findViewById(R.id.img_roleIconItem)

    }

}