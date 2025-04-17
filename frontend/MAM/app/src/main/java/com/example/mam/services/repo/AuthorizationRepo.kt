package com.example.mam.services.repo

import android.util.Log
import com.example.mam.model.SignInState
import com.example.mam.model.SignUpState
import com.example.mam.services.RetrofitClient
import com.example.mam.services.request.SignInRequest
import kotlinx.coroutines.flow.MutableStateFlow

open class AuthorizationRepo {
    private val _signInState = MutableStateFlow(SignInState())
    open suspend fun SignIn(){
        try {
            val request = SignInRequest(_signInState.value.username, _signInState.value.password)
            val response = RetrofitClient.api.login(request)

            Log.d("LOGIN", "AccessToken: ${response.accessToken}")
            Log.d("LOGIN", "RefreshToken: ${response.refreshToken}")
        } catch (e: Exception) {
            Log.e("LOGIN", "Lỗi khi đăng nhập: ${e.message}")
        }
    }

    private val _signUpState = MutableStateFlow(SignUpState())
}