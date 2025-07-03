package com.example.mam.dto.authentication

data class ForgetPasswordRequest (
    val code: String = "",
    val newPassword: String = "",
)