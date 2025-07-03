package com.example.mam.dto.authentication

data class VerifyOTPRequest(
    val email: String,
    val otp: String,
    val action: String,
)
