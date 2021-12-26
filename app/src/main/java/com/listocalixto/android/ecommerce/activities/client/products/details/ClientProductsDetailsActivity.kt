package com.listocalixto.android.ecommerce.activities.client.products.details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.models.Product
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar

class ClientProductsDetailsActivity : AppCompatActivity() {

    private val gson = Gson()
    private var selectedProducts = arrayListOf<Product>()

    private var product: Product? = null
    private var count = 1
    private var productPrice = 0.0
    private var sharedPref: SharedPref? = null

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
        initSharedPref()
        setupView()
        getDataFromIntent()
        setData()
        getProductsFromSharedPref()

        addProduct.setOnClickListener { addItem() }
        removeProduct.setOnClickListener { removeItem() }
        btnSaveInMyList.setOnClickListener { addToBag() }
    }

    private fun initSharedPref() {
        sharedPref = SharedPref(this)
    }

    private fun getDataFromIntent() {
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

    private fun addToBag() {
        val index = getIndexOf(product?.id!!)

        if (index == -1) {
            if (product?.quantity == null) {
                product?.quantity = 1
            }
            selectedProducts.add(product!!)
        } else { //If the product already exist in the sharedPref
            selectedProducts[index].quantity = count
        }

        sharedPref?.save("order", selectedProducts)
        showSnackbar(
            layout,
            R.string.product_added,
            Snackbar.LENGTH_SHORT,
            addProduct,
            false
        )
    }

    @SuppressLint("SetTextI18n")
    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getData("order").isNullOrBlank()) {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)
            val index = getIndexOf(product?.id!!)

            if (index != -1) {
                product?.quantity = selectedProducts[index].quantity
                quantityProduct.text = "${product?.quantity}"
                productPrice = product?.price!! * product?.quantity!!
                priceProduct.text = "$ $productPrice"
                btnSaveInMyList.text = "Edit product"
                btnSaveInMyList.setBackgroundColor(MaterialColors.getColor(btnSaveInMyList, R.attr.colorAccent))
            }
            selectedProducts.forEach {
                Log.d(TAG, "getProductsFromSharedPref: $it")
            }

        }
    }

    /**
     * This method is to check it this [product] has been saved in the [sharedPref]. If this is
     * the case, then we can update the quantity of products to save.
     * */

    private fun getIndexOf(idProduct: String): Int {
        var pos = 0
        selectedProducts.forEach {
            if (it.id == idProduct) {
                return pos
            }
            pos++
        }

        return -1
    }

    companion object {
        private const val TAG = "ClientProductsDetailsActivity"
    }

}