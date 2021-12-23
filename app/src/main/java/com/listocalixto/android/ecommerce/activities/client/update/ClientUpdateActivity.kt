package com.listocalixto.android.ecommerce.activities.client.update

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.listocalixto.android.ecommerce.R
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

class ClientUpdateActivity : AppCompatActivity() {

    private var usersProvider: UsersProvider? = null
    private var currentUser: User? = null
    private var sharedPref: SharedPref? = null
    private var imageFile: File? = null

    private lateinit var btnUpdate: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var imgProfilePicture: CircleImageView
    private lateinit var inputName: EditText
    private lateinit var inputLastname: EditText
    private lateinit var inputPhone: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var layout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_update)
        initSharedPref()
        getUserFromSession()
        initUsersProvider()
        setupViews()
        setupData()
        setupToolbar()

        imgProfilePicture.setOnClickListener { selectImage() }
        btnUpdate.setOnClickListener { validateInputs() }
        btnCancel.setOnClickListener { onBackPressed() }
    }

    private fun initUsersProvider() {
        usersProvider = UsersProvider(currentUser?.sessionToken)
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
                    fileUri?.let { imageFile = File(it.path!!); imgProfilePicture.setImageURI(it) }
                }
                ImagePicker.RESULT_ERROR -> {
                    showSnackbar(
                        layout,
                        R.string.err_an_error_was_happened,
                        Snackbar.LENGTH_SHORT,
                        btnUpdate,
                        true
                    )
                }
                else -> {
                    showSnackbar(
                        layout,
                        R.string.error_task_cancelled,
                        Snackbar.LENGTH_SHORT,
                        btnUpdate,
                        false
                    )
                }
            }
        }

    private fun setupData() {
        currentUser?.let {
            inputName.setText(it.name)
            inputLastname.setText(it.lastname)
            inputPhone.setText(it.phone)
            Glide.with(this).load(it.image).into(imgProfilePicture)
        } ?: showSnackbar(
            layout,
            R.string.err_an_error_was_happened,
            Snackbar.LENGTH_SHORT,
            btnUpdate,
            true
        )
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.activity_client_update_title)
        toolbar.subtitle = getString(R.string.activity_client_update_subtitle)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initSharedPref() {
        sharedPref = SharedPref(this)
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            currentUser = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun validateInputs() {
        val name = inputName.text.toString()
        val lastname = inputLastname.text.toString()
        val phone = inputPhone.text.toString()

        when {
            name.isEmpty() && lastname.isEmpty() && phone.isEmpty() -> {
                showSnackbar(
                    layout,
                    R.string.err_empty_fields,
                    Snackbar.LENGTH_SHORT,
                    btnUpdate,
                    true
                )
            }

            else -> {
                currentUser?.name = name
                currentUser?.lastname = lastname
                currentUser?.phone = phone
                updateUser()
            }
        }

    }

    private fun updateUser() {
        imageFile?.let {
            usersProvider?.update(it, currentUser!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(TAG, "onResponse: Response - $response")
                    Log.d(TAG, "onResponse: Body - ${response.body()}")
                    if (response.body()?.isSuccess == true) {
                        saveUserInSession(response.body()?.data.toString())
                        showMessageUserInfoUpdated()
                    } else {
                        showSnackbar(
                            layout,
                            R.string.err_missing_permissions,
                            Snackbar.LENGTH_LONG,
                            btnUpdate,
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
                        btnUpdate,
                        true
                    )
                }
            })
        } ?: run {
            usersProvider?.updateWithoutImage(currentUser!!)
                ?.enqueue(object : Callback<ResponseHttp> {
                    override fun onResponse(
                        call: Call<ResponseHttp>,
                        response: Response<ResponseHttp>
                    ) {
                        Log.d(TAG, "onResponse: Response - $response")
                        Log.d(TAG, "onResponse: Body - ${response.body()}")
                        if (response.body()?.isSuccess == true) {
                            saveUserInSession(response.body()?.data.toString())
                            showMessageUserInfoUpdated()
                        } else {
                            showSnackbar(
                                layout,
                                R.string.err_missing_permissions,
                                Snackbar.LENGTH_LONG,
                                btnUpdate,
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
                            btnUpdate,
                            true
                        )
                    }
                })
        }
    }

    private fun showMessageUserInfoUpdated() {
        showSnackbar(
            layout,
            R.string.user_info_updated,
            Snackbar.LENGTH_LONG,
            btnUpdate,
            false
        )
    }

    private fun saveUserInSession(data: String) {
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
    }

    private fun setupViews() {
        btnUpdate = findViewById(R.id.btn_update)
        btnCancel = findViewById(R.id.btn_cancel)
        imgProfilePicture = findViewById(R.id.img_updateProfilePicture)
        inputName = findViewById(R.id.input_updateName)
        inputLastname = findViewById(R.id.input_updateLastname)
        inputPhone = findViewById(R.id.input_updatePhone)
        toolbar = findViewById(R.id.toolbar)
        layout = findViewById(R.id.clientUpdateActivityLayout)
    }

    companion object {
        private const val TAG = "ClientUpdateActivity"
    }

}