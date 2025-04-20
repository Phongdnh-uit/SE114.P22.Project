package com.example.mam.viewmodel.authorization

import androidx.lifecycle.ViewModel
import com.example.mam.model.authorization.ChangePasswordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChangePasswordViewModel(): ViewModel() {
    private val _changePasswordState = MutableStateFlow(ChangePasswordState())
    val changePasswordState = _changePasswordState.asStateFlow()

    fun setUsername(it: String){
        _changePasswordState.update { state -> state.copy(username = it) }
    }
    fun setOldPassword(it: String){
        _changePasswordState.update { state -> state.copy(oldPassword = it) }
    }
    fun setNewPassword(it: String){
        _changePasswordState.update { state -> state.copy(newPassword =  it) }
    }
    fun setRepeatPassword(it: String){
        _changePasswordState.update { state -> state.copy(repeatPassword = it) }
    }
    fun setOTP(it: String){
        _changePasswordState.update { state -> state.copy(oTP = it) }
    }
    fun isPasswordValid(): Boolean{
        return _changePasswordState.value.newPassword.length >= 6
    }
    fun isChangeButtonEnable(): Boolean{
        return (isPasswordValid()
                && changePasswordState.value.newPassword.equals(changePasswordState.value.repeatPassword)
                && changePasswordState.value.newPassword.isNotEmpty()
                && changePasswordState.value.oldPassword.equals(""))
    }
    fun changePassword(){

    }
    fun isOTPValid(): Boolean{
        return true
    }
}