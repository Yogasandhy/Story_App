package com.example.subintermediate.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subintermediate.data.UserRepository
import com.example.subintermediate.data.api.LoginResponse
import com.example.subintermediate.data.api.RegisterResponse
import com.example.subintermediate.data.pref.UserModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> get() = _registerResponse

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password)
                _registerResponse.value = response
            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val jsonObject = errorResponse?.let { JSONObject(it) }
                val message = jsonObject?.getString("message")
                _registerResponse.value = message?.let { RegisterResponse(error = true, message = it) }
            }
        }
    }

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> get() = _loginResponse

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                _loginResponse.value = response
            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val jsonObject = errorResponse?.let { JSONObject(it) }
                val message = jsonObject?.getString("message")
                _loginResponse.value = LoginResponse(error = true, message = message)
            }
        }
    }

}
