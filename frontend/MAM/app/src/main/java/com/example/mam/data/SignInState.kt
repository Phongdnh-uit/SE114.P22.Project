package com.example.mam.data

data class SignInState (
    val username: String = "",
    val password: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
)