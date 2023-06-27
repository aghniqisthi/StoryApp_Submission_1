package com.example.storyappdicoding.ui

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityAddStoryBinding
import com.example.storyappdicoding.datastore.DataPreferences
import com.example.storyappdicoding.datastore.PreferenceViewModel
import com.example.storyappdicoding.datastore.ViewModelFactory
import com.example.storyappdicoding.reduceFileImage
import com.example.storyappdicoding.rotateFile
import com.example.storyappdicoding.uriToFile
import com.example.storyappdicoding.viewmodel.StoryViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        animation()

        val pref = DataPreferences.getInstance(dataStore)
        val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        prefVM.getName().observe(this) { name ->
            binding.tvUsername.text = name
        }

        binding.ivAddCamera.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            } else startCameraX()
        }

        binding.ivAddGallery.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            } else startGallery()
        }

        binding.buttonAdd.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val desc = binding.edAddDescription.text.toString()
            val description = desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val storyVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[StoryViewModel::class.java]
            val pref = DataPreferences.getInstance(dataStore)
            val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

            prefVM.getToken().observe(this) { token ->
                storyVM.addStory(imageMultipart, description, token)
            }

            storyVM.addstory.observe(this){
                if(!it.error) {
                    val moveDetail = Intent(this, StoryActivity::class.java)
                    startActivity(moveDetail)
                    finish()
                }
            }
        }
        else Toast.makeText(this, resources.getString(R.string.please_up_ur_pic), Toast.LENGTH_SHORT).show()
    }

    private fun animation(){
        val image = ObjectAnimator.ofFloat(binding.ivImage, View.ALPHA, 1f).setDuration(500)
        val txtaddfrom = ObjectAnimator.ofFloat(binding.textviewaddfrom, View.ALPHA, 1f).setDuration(500)
        val addcam = ObjectAnimator.ofFloat(binding.ivAddCamera, View.ALPHA, 1f).setDuration(500)
        val addgallery = ObjectAnimator.ofFloat(binding.ivAddGallery, View.ALPHA, 1f).setDuration(500)
        val tvusername = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(500)
        val tvadd = ObjectAnimator.ofFloat(binding.layoutDesc, View.ALPHA, 1f).setDuration(500)
        val btnadd = ObjectAnimator.ofFloat(binding.buttonAdd, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(image, txtaddfrom, addcam, addgallery)
        }

        AnimatorSet().apply {
            playSequentially(together, tvusername, tvadd, btnadd)
            start()
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_pic))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                getFile = uriToFile(uri, this)
                binding.ivImage.setImageURI(uri)
            }
        }
    }


    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, resources.getString(R.string.didnt_get_permission), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}