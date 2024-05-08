package com.example.subintermediate.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.subintermediate.data.UserRepository
import com.example.subintermediate.data.api.ListStoryItem
import com.example.subintermediate.data.api.StoryResponse
import com.example.subintermediate.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return repository.getStories()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStoriesWithLocation(): LiveData<StoryResponse> = liveData {
        val response = repository.getStoriesWithLocation()
        emit(response)
    }
}

