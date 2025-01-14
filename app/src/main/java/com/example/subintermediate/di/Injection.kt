package com.example.subintermediate.di

import android.content.Context
import com.example.subintermediate.data.UserRepository
import com.example.subintermediate.data.api.ApiConfig
import com.example.subintermediate.data.pref.UserPreference
import com.example.subintermediate.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }
}
