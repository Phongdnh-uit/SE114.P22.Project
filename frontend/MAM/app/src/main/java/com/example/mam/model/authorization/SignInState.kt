package com.example.mam.model.authorization

data class SignInState (
    val username: String = "",
    val password: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
)