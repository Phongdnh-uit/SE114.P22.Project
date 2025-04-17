package com.example.mam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.model.SignInState
import com.example.mam.model.SignUpState
import com.example.mam.services.repo.AuthorizationRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthorizationViewModel(private val repository: AuthorizationRepo) : ViewModel(){
    //SignIn
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()
    fun checkSignIn(){
        viewModelScope.launch { repository.SignIn() }
    }
    fun setUsername(it: String) {
        _signInState.update { state -> state.copy(username = it) }
    }
    fun setSIPassword(it: String) {
        _signInState.update { state -> state.copy(password = it) }
    }

    //SignUp
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    fun isSignUpButtonEnable(): Boolean{
        return (_signUpState.value.password.isNotEmpty() && (_signUpState.value.repeatPassword.equals(_signUpState.value.password))
                && _signUpState.value.name.isNotEmpty() && _signUpState.value.userName.isNotEmpty()
                && _signUpState.value.phoneNumber.isNotEmpty() && _signUpState.value.email.isNotEmpty())
    }
    fun isRepeatPasswordValid(): Boolean{
        return _signUpState.value.password.equals(_signUpState.value.repeatPassword)
    }
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

}