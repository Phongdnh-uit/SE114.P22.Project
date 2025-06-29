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
import com.example.mam.dto.authentication.FirebaseRegisterRequest
import com.example.mam.dto.authentication.RefreshTokenRequest
import com.example.mam.repository.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class StartViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val accessToken = userPreferencesRepository.accessToken.map { it }
    private val refreshToken = userPreferencesRepository.refreshToken.map { it }

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading


    fun setPhoneNumber(it: String) {
        _phoneNumber.value = it
    }

    fun isPhoneNumberValid(): Boolean {
        val phoneNumber = _phoneNumber.value
        val regex = "^0\\d{9}$".toRegex()
        return regex.matches(phoneNumber)
    }

    fun triggerLoading() {
        _isLoading.value = true
    }

    fun resetLoading() {
        _isLoading.value = false
    }

    suspend fun RegisterWithFireBase(idToken: String): Int {
        _isLoading.value = true
        Log.d("FIREBASE", "ID Token: $idToken")
        if (idToken.isEmpty()) {
            Log.d("FIREBASE", "No ID token found, returning 0")
            _isLoading.value = false
            return 0
        }
        try {
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.registerFirebase(
                FirebaseRegisterRequest(
                    idToken = idToken,
                    phoneNumber = _phoneNumber.value)
            )
            val statusCode = response.code()
            Log.d("FIREBASE", "Status Code: $statusCode")
            if (response.isSuccessful) {
                val user = response.body()
                _isLoading.value = false
                return 1
            } else {
                Log.d("FIREBASE", "Status error: ${response.errorBody()?.string()}")
                _isLoading.value = false
                return 0
            }
        } catch (e: Exception) {
            Log.e("FIREBASE", "Lỗi khi đăng nhập bằng Firebase: ${e.message}")
            _isLoading.value = false
            return 0
        }
    }

    suspend fun getAccessToken(): Int {
        Log.d("REFRESH", "DSRefreshToken: ${refreshToken.first()}")
        if (refreshToken.first().isEmpty()) {
            Log.d("REFRESH", "No refresh token found, returning 0")
            return 0
        }
        else {
            try {
                val response =
                    BaseRepository(userPreferencesRepository).authPublicRepository.refreshToken(
                        RefreshTokenRequest(refreshToken = refreshToken.first()))
                val statusCode = response.code()
                if (response.isSuccessful) {
                    val token = response.body()
                    Log.d("REFRESH", "Status Code: $statusCode")
                    userPreferencesRepository.saveAccessToken(
                        token?.accessToken ?: "",
                        token?.refreshToken ?: ""
                    )
                    Log.d("REFRESH", "DSAccessToken: ${accessToken.first()}")
                    Log.d("REFRESH", "DSRefreshToken: ${refreshToken.first()}")
                    val me = BaseRepository(userPreferencesRepository).authPrivateRepository.getUserInfo().body()!!
                    _isLoading.value = false
                    return if (me.role.name == "ADMIN") 1
                    else 2
                } else {
                    Log.d("REFRESH", "Status Code: $statusCode")
                    _isLoading.value = false
                    return 0
                }
            } catch (e: Exception) {
                Log.e("REFRESH", "Lỗi khi refresh: ${e.message}")
                _isLoading.value = false
                return 0
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                StartViewModel(application.userPreferencesRepository)
            }
        }
    }
}