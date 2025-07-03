package com.example.mam.dto.authentication

data class SignUpRequest(
    val fullname: String = "",
    val phone: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = ""
)
