package com.listocalixto.android.ecommerce.activities.client.products.details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.models.Product

class ClientProductsDetailsActivity : AppCompatActivity() {

    private var product: Product? = null
    private var count = 1
    private var productPrice = 0.0

    private lateinit var layout: CoordinatorLayout
    private lateinit var imageSlider: ImageSlider
    private lateinit var nameProduct: TextView
    private lateinit var descriptionProduct: TextView
    private lateinit var addProduct: ImageView
    private lateinit var quantityProduct: TextView
    private lateinit var removeProduct: ImageView
    private lateinit var priceProduct: TextView
    private lateinit var btnSaveInMyList: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_details)
        setupView()
        getDataFromIntent()
        setData()

        addProduct.setOnClickListener { addItem() }
        removeProduct.setOnClickListener { removeItem() }
    }

    private fun getDataFromIntent() {
        val gson = Gson()
        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)
        product?.let { productPrice = it.price }
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        quantityProduct.text = count.toString()
        product?.let {
            val imageList = arrayListOf(
                SlideModel(it.image00, ScaleTypes.CENTER_CROP),
                SlideModel(it.image01, ScaleTypes.CENTER_CROP),
                SlideModel(it.image02, ScaleTypes.CENTER_CROP)
            )

            imageSlider.setImageList(imageList)
            nameProduct.text = it.name
            descriptionProduct.text = it.description
            priceProduct.text = "$ ${it.price}"

        } ?: run {

        }
    }

    private fun setupView() {
        layout = findViewById(R.id.clientProductsDetailsLayout)
        imageSlider = findViewById(R.id.imageSlider)
        nameProduct = findViewById(R.id.nameProduct)
        descriptionProduct = findViewById(R.id.descriptionProduct)
        addProduct = findViewById(R.id.img_addProduct)
        quantityProduct = findViewById(R.id.quantityProduct)
        removeProduct = findViewById(R.id.img_removeProduct)
        priceProduct = findViewById(R.id.priceProduct)
        btnSaveInMyList = findViewById(R.id.btn_saveProductInMyList)
    }

    @SuppressLint("SetTextI18n")
    private fun addItem() {
        count++
        productPrice = product?.price!! * count
        product?.quantity = count
        quantityProduct.text = "${product?.quantity}"
        priceProduct.text = "$ $productPrice"
    }

    @SuppressLint("SetTextI18n")
    private fun removeItem() {
        if (count > 1) {
            count--
            productPrice = product?.price!! * count
            product?.quantity = count
            quantityProduct.text = "${product?.quantity}"
            priceProduct.text = "$ $productPrice"
        }
    }

}