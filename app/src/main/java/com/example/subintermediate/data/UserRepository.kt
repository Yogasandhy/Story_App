package com.example.subintermediate.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.subintermediate.data.api.ApiConfig
import com.example.subintermediate.data.api.ApiService
import com.example.subintermediate.data.api.LoginResponse
import com.example.subintermediate.data.api.RegisterResponse
import com.example.subintermediate.data.api.StoryResponse
import com.example.subintermediate.data.pref.UserModel
import com.example.subintermediate.data.pref.UserPreference
import com.example.subintermediate.ui.story.StoryPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private var apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
        if (user.token.isNotEmpty()) {
            apiService = ApiConfig.getApiService(user.token)
        }
    }

    fun getApiService(): ApiService {
        return apiService
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }


    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.clearSession()
    }

    suspend fun addStory(description: String, photo: File, lat: Float?, lon: Float?): StoryResponse {
        val descriptionPart = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val photoPart = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            RequestBody.create("image/*".toMediaTypeOrNull(), photo)
        )
        val latPart = lat?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it.toString()) }
        val lonPart = lon?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it.toString()) }

        return apiService.addStory(descriptionPart, photoPart, latPart, lonPart)
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    fun getStories() = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { StoryPagingSource(apiService) }
    ).liveData

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}