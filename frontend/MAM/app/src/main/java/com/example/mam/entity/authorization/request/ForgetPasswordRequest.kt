package com.example.mam.entity.authorization.request

data class ForgetPasswordRequest (
    val username: String = "",
    val newPassword: String = "",
)