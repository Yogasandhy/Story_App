package com.example.subintermediate.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.subintermediate.databinding.ActivityDetailBinding
import com.example.subintermediate.di.Injection
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("EXTRA_DATA")

        val userRepository = Injection.provideRepository(this)
        val apiService = userRepository.getApiService()

        lifecycleScope.launch {
            val response = storyId?.let { apiService.getStoryDetail(it) }

            if (response != null) {
                if (response.error == false) {
                    val story = response.story
                    Glide.with(this@DetailActivity).load(story?.photoUrl).into(binding.stry)
                    binding.username.text = story?.name
                    binding.desc.text = story?.description
                } else {
                    Toast.makeText(this@DetailActivity, "Terjadi kesalahan saat memuat detail cerita", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
