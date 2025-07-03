package com.example.mam.dto.authentication

data class SendOTPRequest(
    val email: String,
    val action: String = "",
)
