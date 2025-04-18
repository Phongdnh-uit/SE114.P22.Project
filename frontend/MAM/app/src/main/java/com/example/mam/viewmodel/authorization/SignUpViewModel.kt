package com.example.mam.viewmodel.authorization

import androidx.lifecycle.ViewModel
import com.example.mam.model.authorization.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel(): ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    fun setName(it: String) {
        _signUpState.update { state -> state.copy(name = it) }
    }
    fun setPhoneNumber(it: String) {
        _signUpState.update { state -> state.copy(phoneNumber = it) }
    }
    fun setEmail(it: String) {
        _signUpState.update { state -> state.copy(email = it) }
    }
    fun setUserName(it: String) {
        _signUpState.update { state -> state.copy(userName = it) }
    }
    fun setSUPassword(it: String) {
        _signUpState.update { state -> state.copy(password = it) }
    }
    fun setRepeatPassword(it: String) {
        _signUpState.update { state -> state.copy(repeatPassword = it) }
    }
    fun isPasswordValid(): Boolean{
        return _signUpState.value.password.length >= 6
    }
    fun isSignUpButtonEnable(): Boolean{
        return (isPasswordValid()
                && _signUpState.value.password.isNotEmpty()
                && _signUpState.value.repeatPassword.equals(_signUpState.value.password)
                && _signUpState.value.name.isNotEmpty()
                && _signUpState.value.userName.isNotEmpty()
                && _signUpState.value.phoneNumber.isNotEmpty()
                && _signUpState.value.email.isNotEmpty())
    }
    fun isRepeatPasswordValid(): Boolean{
        return _signUpState.value.password.equals(_signUpState.value.repeatPassword)
    }
}