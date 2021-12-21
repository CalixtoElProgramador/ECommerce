package com.listocalixto.android.ecommerce.activities

import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.ecommerce.R
import com.listocalixto.android.ecommerce.activities.client.ClientActivity
import com.listocalixto.android.ecommerce.util.showSnackbar
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class SaveImageActivity : AppCompatActivity() {

    private var imageFile: File? = null
    private var circleImageUser: CircleImageView? = null
    private var btnSkip: MaterialButton? = null
    private var btnConfirm: MaterialButton? = null

    private lateinit var layout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)
        setupViews()

        circleImageUser?.setOnClickListener { selectImage() }
        btnSkip?.setOnClickListener { navigateToClientActivity() }
        btnConfirm?.setOnClickListener {  }

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

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                fileUri?.let { imageFile = File(it.path!!); circleImageUser?.setImageURI(it) }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showSnackbar(
                    layout,
                    R.string.err_an_error_was_happened,
                    Snackbar.LENGTH_SHORT,
                    btnConfirm,
                    true
                )
            } else {
                showSnackbar(
                    layout,
                    R.string.error_task_cancelled,
                    Snackbar.LENGTH_SHORT,
                    btnConfirm,
                    false
                )
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
}