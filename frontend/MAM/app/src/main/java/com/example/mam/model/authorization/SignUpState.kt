package com.example.mam.model.authorization

data class SignUpState (
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val userName: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)