package com.example.mam.dto.authentication

data class ChangePasswordRequest(
    val currentPassword: String = "",
    val newPassword: String = "",
)
