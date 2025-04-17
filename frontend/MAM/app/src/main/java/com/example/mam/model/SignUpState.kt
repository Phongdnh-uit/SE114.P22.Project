package com.example.mam.model

data class SignUpState (
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val userName: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)