package com.listocalixto.android.ecommerce.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.ClientActivity
import com.listocalixto.android.ecommerce.models.ResponseHttp
import com.listocalixto.android.ecommerce.models.User
import com.listocalixto.android.ecommerce.providers.UsersProvider
import com.listocalixto.android.ecommerce.util.SharedPref
import com.listocalixto.android.ecommerce.util.showSnackbar
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {

    private val usersProvider = UsersProvider()

    private var imageFile: File? = null
    private var circleImageUser: CircleImageView? = null
    private var btnSkip: MaterialButton? = null
    private var btnConfirm: MaterialButton? = null
    private var user: User? = null
    private var sharedPref: SharedPref? = null

    private lateinit var layout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)
        initSharedPref()
        getUserFromSession()
        setupViews()

        circleImageUser?.setOnClickListener { selectImage() }
        btnSkip?.setOnClickListener { navigateToClientActivity() }
        btnConfirm?.setOnClickListener { validateImage() }

    }

    private fun initSharedPref() {
        sharedPref = SharedPref(this)
    }

    private fun validateImage() {
        if (imageFile == null || user == null) {
            showSnackbar(layout, R.string.err_missing_image, Snackbar.LENGTH_SHORT, btnConfirm, true)
            return
        }
        saveImage()
    }

    private fun saveImage() {
        usersProvider.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d(TAG, "onResponse: Response - $response")
                Log.d(TAG, "onResponse: Body - ${response.body()}")
                saveUserInSession(response.body()?.data.toString())
                navigateToClientActivity()
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}", t)
                showSnackbar(
                    layout,
                    R.string.err_an_error_was_happened,
                    Snackbar.LENGTH_SHORT,
                    btnConfirm,
                    true
                )
            }
        })
    }

    private fun saveUserInSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun navigateToClientActivity() {
        val i = Intent(this, ClientActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        finish()
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data
                    fileUri?.let { imageFile = File(it.path!!); circleImageUser?.setImageURI(it) }
                }
                ImagePicker.RESULT_ERROR -> {
                    showSnackbar(
                        layout,
                        R.string.err_an_error_was_happened,
                        Snackbar.LENGTH_SHORT,
                        btnConfirm,
                        true
                    )
                }
                else -> {
                    showSnackbar(
                        layout,
                        R.string.error_task_cancelled,
                        Snackbar.LENGTH_SHORT,
                        btnConfirm,
                        false
                    )
                }
            }
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

    private fun setupViews() {
        circleImageUser = findViewById(R.id.img_selectProfilePicture)
        btnConfirm = findViewById(R.id.btn_saveImage)
        btnSkip = findViewById(R.id.btn_skipProfilePicture)
        layout = findViewById(R.id.saveImageActivityLayout)
    }
    
    companion object {
        private const val TAG = "SaveImageActivity"
    }
    
}