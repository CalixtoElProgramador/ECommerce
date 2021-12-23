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
import com.listocalixto.android.ecommerce.models.Category
import com.listocalixto.android.ecommerce.models.Role
import com.listocalixto.android.ecommerce.util.SharedPref

class CategoriesAdapter(private val categories: ArrayList<Category>, private val activity: Activity) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    val sharedPref = SharedPref(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name
        Glide.with(activity).load(category.image).into(holder.imgCategory)

    }

    class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.nameCategoryItem)
        val imgCategory: ImageView = view.findViewById(R.id.img_photoCategoryItem)

    }

}