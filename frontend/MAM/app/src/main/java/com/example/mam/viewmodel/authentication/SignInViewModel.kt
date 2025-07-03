package com.example.mam.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.authentication.FirebaseLoginRequest
import com.example.mam.dto.authentication.SendVerifyEmailRequest
import com.example.mam.dto.authentication.SignInRequest
import com.example.mam.dto.user.UserResponse
import com.example.mam.dto.vo.HandleError
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class SignInViewModel(
    // Nhận UserPreferencesRepository từ MAMApplication
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    // Lấy access token và refresh token từ UserPreferencesRepository
    private val accessToken = userPreferencesRepository.accessToken.map { it }
    private val refreshToken = userPreferencesRepository.refreshToken.map { it }

    private val _signInState = MutableStateFlow(SignInRequest())
    val signInState = _signInState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _me = MutableStateFlow(UserResponse())

    fun setUsername(it: String) {
        _signInState.update { state -> state.copy(credentialId = it) }
    }

    fun setSIPassword(it: String) {
        _signInState.update { state -> state.copy(password = it.trim()) }
    }

    fun triggerLoading() {
        _isLoading.value = true
    }

    fun resetLoading() {
        _isLoading.value = false
    }

    suspend fun SignIn(): Pair<String,String> {
        try {
            val request = _signInState.value
            Log.d("LOGIN", "CredentialID: ${request.credentialId}")
            Log.d("LOGIN", "Password: ${request.password}")

            // Gọi service đăng nhập
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.login(request)
            val statusCode = response.code()
            Log.d("LOGIN", "Status Code: $statusCode")

            if (response.isSuccessful) {
                val token = response.body()
                if (token != null) {
                    _me.value = token.user
                }
                Log.d("LOGIN", "AccessToken: ${token?.accessToken}")
                Log.d("LOGIN", "RefreshToken: ${token?.refreshToken}")
                // Lưu access token và refresh token vào DataStore
                userPreferencesRepository.saveAccessToken(token?.accessToken ?: "", token?.refreshToken ?:"")
                Log.d("LOGIN", "DSAccessToken: ${accessToken.first()}")
                Log.d("LOGIN", "DSRefreshToken: ${refreshToken.first()}")
                _isLoading.value = false
                return Pair(_me.value.role.name,_me.value.status)
            }
            else{
                Log.e("LOGIN", "Đăng nhập thất bại với mã lỗi: ${response.errorBody()?.string()}")
                _isLoading.value = false
                return Pair("ERROR",HandleError(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Lỗi khi đăng nhập: ${e.message}")
            _isLoading.value = false
            return Pair("ERROR","Đăng nhập thất bại. Vui lòng thử lại sau.")
        }
    }

    suspend fun signInWithFirebase(idToken: String): Pair<String,String> {
        Log.d("LOGIN", "ID Token: $idToken")
        try {
            val request = FirebaseLoginRequest(idToken)
            // Gọi service đăng nhập với Firebase
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.loginFirebase(request)
            val statusCode = response.code()
            Log.d("LOGIN", "Status Code: $statusCode")

            if (response.isSuccessful) {
                val token = response.body()
                if (token != null) {
                    _me.value = token.user
                }
                Log.d("LOGIN", "AccessToken: ${token?.accessToken}")
                Log.d("LOGIN", "RefreshToken: ${token?.refreshToken}")
                Log.d("LOGIN", "User: ${_me.value}")
                // Lưu access token và refresh token vào DataStore
                userPreferencesRepository.saveAccessToken(token?.accessToken ?: "", token?.refreshToken ?:"")
                Log.d("LOGIN", "DSAccessToken: ${accessToken.first()}")
                Log.d("LOGIN", "DSRefreshToken: ${refreshToken.first()}")
                _isLoading.value = false
                return Pair(_me.value.role.name,_me.value.status)
            }
            else{
                Log.e("LOGIN", "Đăng nhập thất bại với mã lỗi: ${response.errorBody()?.string()}")
                _isLoading.value = false
                return Pair("ERROR",HandleError(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Lỗi khi đăng nhập: ${e.message}")
            _isLoading.value = false
            return Pair("ERROR","Đăng nhập thất bại. Vui lòng thử lại sau.")
        }
    }

    suspend fun resendVerificationEmail() : Int {
        try {
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.sendVerifyEmail(
                SendVerifyEmailRequest(
                    email = _me.value.email
                )
            )
            if (response.isSuccessful) {
                Log.d("LOGIN", "Email xác thực đã được gửi lại thành công.")
                _isLoading.value = false
                return 1
            } else {
                Log.e("LOGIN", "Gửi lại email xác thực thất bại: ${response.errorBody()?.string()}")
                _isLoading.value = false
                return 0
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Lỗi khi gửi lại email xác thực: ${e.message}")
            _isLoading.value = false
            return 0
        }
    }
    // Factory để khởi tạo ViewModel với tham số là UserPreferencesRepository
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                SignInViewModel(application.userPreferencesRepository)
            }
        }
    }
}