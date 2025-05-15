package com.example.mam.viewmodel.authorization

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mam.entity.authorization.request.SignInRequest
import com.example.mam.entity.authorization.request.SignUpRequest
import com.example.mam.services.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SignUpViewModel(): ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpRequest())
    val signUpState = _signUpState.asStateFlow()
    private val _repeatPassword = MutableStateFlow("")
    val repeatPassword = _repeatPassword.asStateFlow()

    fun setName(it: String) {
        _signUpState.update { state -> state.copy(fullName = it) }
    }

    fun setPhoneNumber(it: String) {
        _signUpState.update { state -> state.copy(phoneNumber = it) }
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
        val fullname = _signUpState.value.fullName.trim()
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
        val phoneNumber = _signUpState.value.phoneNumber.trim()
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

    suspend fun SignUp(): Int {
        return withContext(Dispatchers.IO) {
            try {
                1
            } catch (e: Exception) {
                Log.e("LOGIN", "Lỗi khi đăng nhập: ${e.message}")
                0
            }
        }
    }

    fun notifySignUpFalse(){

    }
}