package com.aulmrd.mystory.ui.story

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.aulmrd.mystory.data.result.Result
import com.aulmrd.mystory.databinding.ActivityStoryBinding
import com.aulmrd.mystory.ui.factory.FactoryStoryViewModel
import com.aulmrd.mystory.ui.login.LoginActivity
import com.aulmrd.mystory.utils.MediaUtils
import com.aulmrd.mystory.utils.MediaUtils.rotateBitmap
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var token: String
    private lateinit var currentPhotoPath: String
    private lateinit var result: Bitmap
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        setupViewModel()
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnSubmit.setOnClickListener { uploadImage() }
    }

    private fun setupViewModel(){
        val factoryStoryViewModel: FactoryStoryViewModel = FactoryStoryViewModel.getInstance(this)
        storyViewModel = ViewModelProvider(this, factoryStoryViewModel)[StoryViewModel::class.java]

        storyViewModel.getToken().observe(this){token ->
            if (token.isEmpty()){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                this.token = token
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "cant get permission", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadImage() {
        if (getFile != null) {
            val description = binding.etDescription.text.toString().trim()
            if (description.isEmpty()){
                binding.etDescription.error = "Enter your {description}"
            } else {
                binding.progressBar.visibility = View.VISIBLE
                val file = MediaUtils.reduceFileImage(getFile as File)
                val descMedia = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg" .toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

                storyViewModel.uploadStory(token, imageMultipart, descMedia).observe(this){ result ->
                    if (result != null){
                        when(result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Failure : " + result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please upload your image!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun  startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = MediaUtils.uriToFile(selectedImg, this@StoryActivity)
            getFile = myFile
            binding.ivStory.setImageURI(selectedImg)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        MediaUtils.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(this@StoryActivity, "com.aulmrd.mystory.ui.story", it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.btnSubmit.isEnabled = true
            binding.ivStory.setImageBitmap(result)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.ivStory.setImageBitmap(result)
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        const val CAMERA_X_RESULT = 200
    }
}