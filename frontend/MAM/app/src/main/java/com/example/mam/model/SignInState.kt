package com.example.mam.model

data class SignInState (
    val username: String = "",
    val password: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
)