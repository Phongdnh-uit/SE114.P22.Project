package com.example.mam.services.dto.authorization.request

data class SignUpRequest(
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = ""
)
