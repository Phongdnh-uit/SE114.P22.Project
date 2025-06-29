package com.example.mam.dto.authentication

data class FirebaseRegisterRequest(
    val idToken: String = "",
    val phoneNumber: String = "",
)
