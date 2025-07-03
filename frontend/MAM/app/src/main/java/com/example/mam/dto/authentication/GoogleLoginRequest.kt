package com.example.mam.dto.authentication

data class GoogleLoginRequest(
    val clientId: String,
    val credential: String,
)

