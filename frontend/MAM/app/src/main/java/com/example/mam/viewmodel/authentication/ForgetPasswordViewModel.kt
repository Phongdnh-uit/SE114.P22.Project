package com.example.mam.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.Constant
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.authentication.ChangePasswordRequest
import com.example.mam.dto.authentication.SendOTPRequest
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgetPasswordViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _repeatPassword = MutableStateFlow("")
    val repeatPassword = _repeatPassword.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _oldPassword = MutableStateFlow("")
    val oldPassword = _oldPassword.asStateFlow()

    private val _code = MutableStateFlow("")
    val code = _code.asStateFlow()

    fun setOldPassword(it: String) {
        _oldPassword.value = it.trim()
    }

    fun setEmail(it: String){
        _email.value = it.trim()
    }

    fun setNewPassword(it: String){
        _password.value = it.trim()
    }

    fun setRepeatPassword(it: String){
        _repeatPassword.value = it.trim()
    }

    fun isEmailValid(): Boolean {
        val email = _email.value.trim()
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return regex.matches(email)
    }

    fun isPasswordValid(): Boolean{
        return _password.value.length >= 6
    }

    fun isRepeatPasswordValid(): Boolean {
        val password = _password.value.trim()
        val repeatPassword = _repeatPassword.value.trim()
        return password == repeatPassword
    }

    fun isChangeButtonEnable(isForgot: Boolean): Boolean{
        return (isPasswordValid()
                && isRepeatPasswordValid()
                && !(isForgot xor isEmailValid()))
    }


    suspend fun sendOTP(): Int {
        try {
            val metadata = BaseRepository(userPreferencesRepository).authPublicRepository.getMetadata(listOf(
                Constant.metadata.OTP_ACTION.name
            ))
            Log.d("OtpViewModel", "Get metadata: ${metadata.code()}")
            val action = metadata.body()?.get(Constant.metadata.OTP_ACTION.name)?.get(0) ?: ""
            if (!metadata.isSuccessful) {
                Log.d("OtpViewModel", "Error getting metadata: ${metadata.errorBody()?.string()}")
                return 0
            }
            Log.d("OtpViewModel", "Metadata: $action")
            val request = SendOTPRequest(
                _email.value,
                action
            )
            Log.d("ForgetPasswordViewModel", "Sending OTP with request: ${request.email}, ${request.action}")
            val respond = BaseRepository(userPreferencesRepository).authPublicRepository.sendOtp(
                request
            )
            Log.d("ForgetPasswordViewModel", "Send OTP response: ${respond.code()}")
            return if (respond.isSuccessful) {
                Log.d("ForgetPasswordViewModel", "OTP sent successfully")
                1
            } else {
                Log.d("ForgetPasswordViewModel", "Failed to send OTP: ${respond.errorBody()?.string()}")
                0
            }
        } catch (e: Exception) {
            Log.d("ForgetPasswordViewModel", "Error sending OTP: ${e.message}")
            return 0
        }
    }
    suspend fun changePassword(): Int{
        try {
            val request = ChangePasswordRequest(
                _oldPassword.value,
                _password.value,
            )
            Log.d("ForgetPasswordViewModel", "Changing password with request: ${request.currentPassword}, ${request.newPassword}")
            val respond = BaseRepository(userPreferencesRepository).authPrivateRepository.changePassword(request)
            Log.d("ForgetPasswordViewModel", "Change password response: ${respond.code()}")
            return if (respond.isSuccessful) {
                Log.d("ForgetPasswordViewModel", "Password changed successfully")
                1
            } else {
                Log.d("ForgetPasswordViewModel", "Failed to change password: ${respond.errorBody()?.string()}")
                0
            }
        }
        catch (e: Exception) {
            Log.d("ForgetPasswordViewModel", "Error changing password: ${e.message}")
            return 0
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ForgetPasswordViewModel(application.userPreferencesRepository)
            }
        }
    }
}