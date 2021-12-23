package com.listocalixto.android.ecommerce.fragments.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.adapters.CategoriesAdapter
import com.listocalixto.android.ecommerce.fragments.client.ClientCategoriesFragment
import com.listocalixto.android.ecommerce.models.Category
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.CategoriesProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantProductFragment : Fragment(R.layout.fragment_restaurant_product) {

    private var categoriesProvider: CategoriesProvider? = null
    private var currentUser: User? = null
    private var sharedPref: SharedPref? = null
    private var imgFile00: File? = null
    private var imgFile01: File? = null
    private var imgFile02: File? = null
    private var idCategory = ""

    private lateinit var layout: CoordinatorLayout
    private lateinit var btnSave: MaterialButton
    private lateinit var img00: ImageView
    private lateinit var img01: ImageView
    private lateinit var img02: ImageView
    private lateinit var inputName: EditText
    private lateinit var inputDescription: EditText
    private lateinit var inputPrice: EditText
    private lateinit var spinnerCategories: Spinner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPref()
        getUserFromSession()
        initCategoriesProvider()
        setupViews(view)
        getCategoriesFromProvider()

        btnSave.setOnClickListener { validateInputs() }
        img00.setOnClickListener { selectImage(1000) }
        img01.setOnClickListener { selectImage(1111) }
        img02.setOnClickListener { selectImage(1222) }

    }

    private fun initSharedPref() {
        sharedPref = SharedPref(requireActivity())
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            currentUser = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun initCategoriesProvider() {
        categoriesProvider = CategoriesProvider(currentUser?.sessionToken!!)
    }

    private fun getCategoriesFromProvider() {
        categoriesProvider?.getAll()?.enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {
                response.body()?.let {
                    val spinnerLayout = android.R.layout.simple_dropdown_item_1line
                    val arrayAdapter = ArrayAdapter(requireActivity(), spinnerLayout, it)
                    spinnerCategories.adapter = arrayAdapter
                    spinnerCategories.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
                                idCategory = it[position].id.toString()
                                Log.d(TAG, "onItemSelected: $idCategory")
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }
                        }
                } ?: run {
                    showSnackbar(
                        layout,
                        R.string.err_missing_permissions,
                        Snackbar.LENGTH_LONG,
                        btnSave,
                        true
                    )
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}", t)
                showSnackbar(
                    layout,
                    R.string.err_an_error_was_happened,
                    Snackbar.LENGTH_SHORT,
                    btnSave,
                    true
                )
            }
        })
    }

    private fun validateInputs() {
        val name = inputName.text.toString()
        val description = inputDescription.text.toString()
        val priceText = inputPrice.text.toString()

        when {
            name.isEmpty() || description.isEmpty() || priceText.isEmpty() -> {
                showSnackbar(
                    layout,
                    R.string.err_empty_fields,
                    Snackbar.LENGTH_SHORT,
                    btnSave,
                    true
                )
            }
            imgFile00 == null && imgFile01 == null && imgFile02 == null -> {
                showSnackbar(
                    layout,
                    R.string.err_at_least_one_image_is_required,
                    Snackbar.LENGTH_SHORT,
                    btnSave,
                    true
                )
            }

            else -> {
                saveProduct(/*name, description, priceText*/)
            }

        }

    }

    private fun saveProduct(/*name: String, description: String, priceText: String*/) {
        /*
        val file00 = imgFile00
        val file01 = imgFile01
        val file02 = imgFile02
        */

        showSnackbar(
            layout,
            R.string.app_name,
            Snackbar.LENGTH_SHORT,
            btnSave,
            false
        )
    }

    private fun setupViews(view: View) {
        layout = view.findViewById(R.id.restaurantProductLayout)
        btnSave = view.findViewById(R.id.btn_saveProduct)
        img00 = view.findViewById(R.id.img_photoProduct_00)
        img01 = view.findViewById(R.id.img_photoProduct_01)
        img02 = view.findViewById(R.id.img_photoProduct_02)
        inputName = view.findViewById(R.id.input_nameProduct)
        inputDescription = view.findViewById(R.id.input_descriptionProduct)
        inputPrice = view.findViewById(R.id.input_priceProduct)
        spinnerCategories = view.findViewById(R.id.spinner_categories)
    }

    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                when (requestCode) {
                    1000 -> {
                        imgFile00 = File(fileUri?.path!!)
                        img00.setImageURI(fileUri)
                    }
                    1111 -> {
                        imgFile01 = File(fileUri?.path!!)
                        img01.setImageURI(fileUri)
                    }
                    1222 -> {
                        imgFile02 = File(fileUri?.path!!)
                        img02.setImageURI(fileUri)
                    }
                }
            }
            ImagePicker.RESULT_ERROR -> {
                showSnackbar(
                    layout,
                    R.string.err_an_error_was_happened,
                    Snackbar.LENGTH_SHORT,
                    btnSave,
                    true
                )
            }
            else -> {
                showSnackbar(
                    layout,
                    R.string.error_task_cancelled,
                    Snackbar.LENGTH_SHORT,
                    btnSave,
                    true
                )
            }
        }
    }

    companion object {
        private const val TAG = "RestaurantProductFragment"
    }

}