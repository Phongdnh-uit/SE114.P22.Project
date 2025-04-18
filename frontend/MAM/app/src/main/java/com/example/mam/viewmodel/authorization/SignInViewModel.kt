package com.example.mam.viewmodel.authorization

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.model.authorization.SignInState
import com.example.mam.services.RetrofitClient
import com.example.mam.services.request.SignInRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel() : ViewModel() {
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()
    fun setUsername(it: String) {
        _signInState.update { state -> state.copy(username = it) }
    }
    fun setSIPassword(it: String) {
        _signInState.update { state -> state.copy(password = it) }
    }
    fun checkSignIn(){
        viewModelScope.launch {
            try {
                val request = SignInRequest(_signInState.value.username, _signInState.value.password)
                val response = RetrofitClient.api.login(request)

                Log.d("LOGIN", "AccessToken: ${response.accessToken}")
                Log.d("LOGIN", "RefreshToken: ${response.refreshToken}")
            } catch (e: Exception) {
                Log.e("LOGIN", "Lỗi khi đăng nhập: ${e.message}")
            }
        }
    }

}