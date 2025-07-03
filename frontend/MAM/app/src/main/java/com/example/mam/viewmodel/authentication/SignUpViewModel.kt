package com.example.mam.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.authentication.SignUpRequest
import com.example.mam.dto.vo.HandleError
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpRequest())
    val signUpState = _signUpState.asStateFlow()
    private val _repeatPassword = MutableStateFlow("")
    val repeatPassword = _repeatPassword.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun triggerLoading() {
        _isLoading.value = true
    }

    fun resetLoading() {
        _isLoading.value = false
    }

    fun setName(it: String) {
        _signUpState.update { state -> state.copy(fullname = it) }
    }

    fun setPhoneNumber(it: String) {
        _signUpState.update { state -> state.copy(phone = it) }
    }

    fun setEmail(it: String) {
        _signUpState.update { state -> state.copy(email = it) }
    }

    fun setUserName(it: String) {
        _signUpState.update { state -> state.copy(username = it) }
    }

    fun setSUPassword(it: String) {
        _signUpState.update { state -> state.copy(password = it.trim()) }
    }

    fun setRepeatPassword(it: String) {
        _repeatPassword.value = it.trim()
    }

    fun isPasswordValid(): Boolean {
        val password = _signUpState.value.password.trim()
        return password.length >= 6
    }

    fun isUserNameValid(): Boolean {
        val username = _signUpState.value.username.trim()
        val regex = "^[\\p{L}0-9 ]+$".toRegex()
        return username.isNotBlank()
                && username.length in 2..50
                && regex.matches(username)
    }

    fun isFullNameValid(): Boolean {
        val fullname = _signUpState.value.fullname.trim()
        val regex = "^[\\p{L} ]+$".toRegex()
        return fullname.isNotBlank()
                && fullname.length in 2..100
                && regex.matches(fullname)
    }

    fun isRepeatPasswordValid(): Boolean {
        val password = _signUpState.value.password.trim()
        val repeatPassword = _repeatPassword.value.trim()
        return password == repeatPassword
    }

    fun isPhoneNumberValid(): Boolean {
        val phoneNumber = _signUpState.value.phone.trim()
        val regex = "^0\\d{9}$".toRegex()
        return regex.matches(phoneNumber)
    }

    fun isEmailValid(): Boolean {
        val email = _signUpState.value.email.trim()
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return regex.matches(email)
    }

    fun isSignUpButtonEnable(): Boolean{
        return (isPasswordValid()
                && isRepeatPasswordValid()
                && isFullNameValid()
                && isUserNameValid()
                && isPhoneNumberValid()
                && isEmailValid())
    }

    suspend fun signUp(): String {
        try {
            val request = _signUpState.value

            // Gọi service đăng nhập
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.signUp(request)
            val statusCode = response.code()
            Log.d("SignUp", "Status Code: $statusCode")

            if (response.isSuccessful) {
                val user = response.body()
                return "SUCCESS"
            }
            else{
                return HandleError(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            Log.e("SignUp", "Lỗi khi đăng ky: ${e.message}")
            return "Đăng ký thất bại. Vui lòng thử lại sau"
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                SignUpViewModel(application.userPreferencesRepository)
            }
        }
    }
}