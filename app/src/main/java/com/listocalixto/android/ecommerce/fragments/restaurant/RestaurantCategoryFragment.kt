package com.listocalixto.android.ecommerce.fragments.restaurant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.models.Category
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.CategoriesProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RestaurantCategoryFragment : Fragment(R.layout.fragment_restaurant_category) {

    private var imgFile: File? = null
    private var sharedPref: SharedPref? = null
    private var currentUser: User? = null
    private var categoriesProvider: CategoriesProvider? = null

    private lateinit var layout: CoordinatorLayout
    private lateinit var imgCategory: ImageView
    private lateinit var inputCategoryName: EditText
    private lateinit var btnSaveCategory: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPref()
        getUserFromSession()
        initCategoriesProvider()
        setupViews(view)

        imgCategory.setOnClickListener { selectImage() }
        btnSaveCategory.setOnClickListener { validateInputs() }

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

    private fun setupViews(view: View) {
        layout = view.findViewById(R.id.restaurantCategoryLayout)
        imgCategory = view.findViewById(R.id.img_category)
        inputCategoryName = view.findViewById(R.id.input_categoryName)
        btnSaveCategory = view.findViewById(R.id.btn_saveCategory)
    }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data
                    fileUri?.let { imgFile = File(it.path!!); imgCategory.setImageURI(it) }
                }
                ImagePicker.RESULT_ERROR -> {
                    showSnackbar(
                        layout,
                        R.string.err_an_error_was_happened,
                        Snackbar.LENGTH_SHORT,
                        btnSaveCategory,
                        true
                    )
                }
                else -> {
                    showSnackbar(
                        layout,
                        R.string.error_task_cancelled,
                        Snackbar.LENGTH_SHORT,
                        btnSaveCategory,
                        false
                    )
                }
            }
        }

    private fun validateInputs() {
        val categoryName = inputCategoryName.text.toString()
        val file = imgFile

        when {
            categoryName.isEmpty() -> {
                showSnackbar(
                    layout,
                    R.string.err_empty_fields,
                    Snackbar.LENGTH_SHORT,
                    btnSaveCategory,
                    true
                )
            }
            file == null -> {
                showSnackbar(
                    layout,
                    R.string.err_missing_image,
                    Snackbar.LENGTH_SHORT,
                    btnSaveCategory,
                    true
                )
            }
            else -> {
                saveCategoryInTheServer(categoryName, file)
            }
        }

    }

    private fun saveCategoryInTheServer(categoryName: String, imgFile: File) {
        categoriesProvider?.create(imgFile, Category(name = categoryName))
            ?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(TAG, "onResponse: Response - $response")
                    Log.d(TAG, "onResponse: Body - ${response.body()}")
                    if (response.body()?.isSuccess == true) {
                        showMessageCategoryWasSaved()
                        cleanInputs()
                    } else {
                        showSnackbar(
                            layout,
                            R.string.err_missing_permissions,
                            Snackbar.LENGTH_LONG,
                            btnSaveCategory,
                            true
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}", t)
                    showSnackbar(
                        layout,
                        R.string.err_an_error_was_happened,
                        Snackbar.LENGTH_SHORT,
                        btnSaveCategory,
                        true
                    )
                }
            })
    }

    private fun cleanInputs() {
        imgFile = null
        inputCategoryName.setText("")
        imgCategory.setImageResource(R.drawable.ic_image)
    }

    private fun showMessageCategoryWasSaved() {
        showSnackbar(
            layout,
            R.string.category_created_successfully,
            Snackbar.LENGTH_LONG,
            btnSaveCategory,
            false
        )
    }

    companion object {
        private const val TAG = "RestaurantCategoryFragment"
    }

}