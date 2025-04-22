package com.example.mam.viewmodel.authorization

import androidx.lifecycle.ViewModel
import com.example.mam.entity.authorization.request.ForgetPasswordRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class ForgetPasswordViewModel(): ViewModel() {
    private val _forgetPasswordState = MutableStateFlow(ForgetPasswordRequest())
    val forgetPasswordState = _forgetPasswordState.asStateFlow()
    private val _repeatPassword = MutableStateFlow("")
    val repeatPassword = _repeatPassword.asStateFlow()
    private val _OTP = MutableStateFlow("")
    var phoneNumber: String = ""
    fun setUsername(it: String){
        _forgetPasswordState.update { state -> state.copy(username = it) }
    }

    fun setNewPassword(it: String){
        _forgetPasswordState.update { state -> state.copy(newPassword =  it.trim()) }
    }

    fun setRepeatPassword(it: String){
        _repeatPassword.value = it.trim()
    }

    fun setOTP(it: String){
        _OTP.value = it
    }

    suspend fun getPhoneNumber(){
        return withContext(Dispatchers.IO) {
            if (isUsernamevalid()) phoneNumber = "0904599204"
            else phoneNumber = "**********"
        }
    }

    fun isUsernamevalid(): Boolean{
        return true
    }

    fun isPasswordValid(): Boolean{
        return _forgetPasswordState.value.newPassword.length >= 6
    }

    fun isRepeatPasswordValid(): Boolean {
        val password = _forgetPasswordState.value.newPassword.trim()
        val repeatPassword = _repeatPassword.value.trim()
        return password == repeatPassword
    }

    fun isChangeButtonEnable(): Boolean{
        return (isPasswordValid()
                && isRepeatPasswordValid()
                && isUsernamevalid())
    }

    fun isOTPValid(): Boolean{
        return true
    }

    fun changePassword(){

    }

    fun notifyOTPInValid(){

    }
}