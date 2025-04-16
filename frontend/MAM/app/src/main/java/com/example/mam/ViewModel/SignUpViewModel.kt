package com.example.mam.ViewModel

import androidx.lifecycle.ViewModel
import com.example.mam.data.SignInState
import com.example.mam.data.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel : ViewModel(){
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    fun isSignUpButtonEnable(): Boolean{
        return !(_signUpState.value.password.isEmpty() || _signUpState.value.repeatPassword.isEmpty()
                || _signUpState.value.name.isEmpty() || _signUpState.value.userName.isEmpty()
                || _signUpState.value.phoneNumber.isEmpty() || _signUpState.value.email.isEmpty())
    }
    fun isRepeatPasswordValid(): Boolean{
        return _signUpState.value.password.equals(_signUpState.value.repeatPassword)
    }
    fun setName(it: String) {
        _signUpState.value = _signUpState.value.copy(name = it)
    }

    fun setPhoneNumber(it: String) {
        _signUpState.value = _signUpState.value.copy(phoneNumber = it)
    }

    fun setEmail(it: String) {
        _signUpState.value = _signUpState.value.copy(email = it)
    }

    fun setUserName(it: String) {
        _signUpState.value = _signUpState.value.copy(userName = it)
    }

    fun setPassword(it: String) {
        _signUpState.value = _signUpState.value.copy(password = it)
    }

    fun setRepeatPassword(it: String) {
        _signUpState.value = _signUpState.value.copy(repeatPassword = it)
    }
}