package com.example.subintermediate.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subintermediate.data.UserRepository
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _addStoryResponse = MutableLiveData<Result<Unit>>()
    val addStoryResponse: LiveData<Result<Unit>> get() = _addStoryResponse

    fun addStory(description: String, photo: File) {
        viewModelScope.launch {
            try {
                userRepository.addStory(description, photo, null, null)
                _addStoryResponse.value = Result.success(Unit)
            } catch (e: Exception) {
                _addStoryResponse.value = Result.failure(e)
            }
        }
    }
}

