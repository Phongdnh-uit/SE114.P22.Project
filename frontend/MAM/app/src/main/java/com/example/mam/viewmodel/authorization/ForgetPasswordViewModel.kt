package com.example.mam.viewmodel.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.authorization.request.ForgetPasswordRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgetPasswordViewModel(): ViewModel() {
    private val _forgetPasswordState = MutableStateFlow(ForgetPasswordRequest())
    val forgetPasswordState = _forgetPasswordState.asStateFlow()
    private val _repeatPassword = MutableStateFlow("")
    val repeatPassword = _repeatPassword.asStateFlow()
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()
    fun setUsername(it: String){
        _forgetPasswordState.update { state -> state.copy(username = it) }
    }

    fun setNewPassword(it: String){
        _forgetPasswordState.update { state -> state.copy(newPassword =  it.trim()) }
    }

    fun setRepeatPassword(it: String){
        _repeatPassword.value = it.trim()
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
    fun fetchPhoneNumber() {
        viewModelScope.launch(Dispatchers.IO) {
            val usernameValid = isUsernamevalid()
            val result = if (usernameValid) "0904599204" else "**********"
            _phoneNumber.emit(result)
        }
    }

//    companion object {
//        suspend fun getPhoneNumber(forgetPasswordViewModel: ForgetPasswordViewModel){
//            return withContext(Dispatchers.IO) {
//                if (forgetPasswordViewModel.isUsernamevalid()) forgetPasswordViewModel.phoneNumber = "0904599204"
//                else forgetPasswordViewModel.phoneNumber = "**********"
//            }
//        }
//    }
}