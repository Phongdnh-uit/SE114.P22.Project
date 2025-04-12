package com.example.mam.ViewModel

import androidx.lifecycle.ViewModel
import com.example.mam.data.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel :ViewModel(){
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()
    fun CheckSignIn(phonenumber: String, password: String){
        _signInState.update {
            currentState ->
            currentState.copy(
                phoneNumber = phonenumber,
                password = password
            )

        }

    }

}