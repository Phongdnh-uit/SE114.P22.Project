package com.example.mam.viewmodel.authorization

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.services.RetrofitClient
import com.example.mam.entity.authorization.request.SignInRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel() : ViewModel() {
    private val _signInState = MutableStateFlow(SignInRequest())
    val signInState = _signInState.asStateFlow()

    fun setUsername(it: String) {
        _signInState.update { state -> state.copy(username = it) }
    }

    fun setSIPassword(it: String) {
        _signInState.update { state -> state.copy(password = it.trim()) }
    }

    suspend fun SignIn(): Int{
        return withContext(Dispatchers.IO) {
            try {
                val request = SignInRequest(_signInState.value.username.trim(), _signInState.value.password)
                val response = RetrofitClient.api.login(request)
                Log.d("LOGIN", "AccessToken: ${response.accessToken}")
                Log.d("LOGIN", "RefreshToken: ${response.refreshToken}")

                if (response.accessToken.isEmpty()) 0 else 1
            } catch (e: Exception) {
                Log.e("LOGIN", "Lỗi khi đăng nhập: ${e.message}")
                0
            }
        }
    }

    fun notifySignInFalse(){

    }

}