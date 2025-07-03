package com.example.mam.dto.authentication

import com.example.mam.dto.user.UserResponse

data class AuthResponse (
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse
)