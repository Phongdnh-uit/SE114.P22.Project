package com.example.mam.entity.authorization.response

data class SignInResponse (
    val accessToken: String,
    val refreshToken: String
)