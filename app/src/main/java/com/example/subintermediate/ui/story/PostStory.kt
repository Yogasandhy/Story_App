package com.example.subintermediate.ui.story

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.subintermediate.databinding.ActivityPostStoryBinding
import com.example.subintermediate.ui.ViewModelFactory
import java.io.File
import java.io.FileOutputStream

class PostStory : AppCompatActivity() {

    private lateinit var binding: ActivityPostStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private var currentImageUri: Uri? = null

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[StoryViewModel::class.java]


        binding.galleryButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            startGallery()
        }

        binding.cameraButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            startCamera()
        }

        binding.uploadButton.setOnClickListener {
            val description = binding.descriptionEditText.text.toString()
            currentImageUri?.let { uri ->
                val file = uriToFile(uri, this)
                if (file.exists()) {
                    binding.progressBar.visibility = View.VISIBLE
                    storyViewModel.addStory(description, file)
                } else {
                    Log.d("Upload", "File does not exist")
                }
            }
        }


        storyViewModel.addStoryResponse.observe(this) { result ->
            if (result.isSuccess) {
                Toast.makeText(this, "Upload berhasil", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Upload gagal: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("Recycle")
    fun uriToFile(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val filename = context.contentResolver.getFileName(uri)
        val file = File(context.cacheDir, filename)
        inputStream?.copyTo(FileOutputStream(file))
        return file
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()

            binding.progressBar.visibility = View.GONE
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        launcherCamera.launch(null)
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            binding.previewImageView.setImageBitmap(bitmap)
            currentImageUri = saveBitmapToFile(bitmap)
            showImage()

            binding.progressBar.visibility = View.GONE
        } else {
            Log.d("Camera", "No photo taken")
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Uri {
        val filename = "${System.currentTimeMillis()}.jpg"
        val file = File(this.cacheDir, filename)
        val out = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.close()

        return Uri.fromFile(file)
    }

    @SuppressLint("Range")
    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }

        if (name.isBlank()) {
            name = "${System.currentTimeMillis()}.jpg"
        }

        return name
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)

            binding.progressBar.visibility = View.GONE
        }
    }
}


