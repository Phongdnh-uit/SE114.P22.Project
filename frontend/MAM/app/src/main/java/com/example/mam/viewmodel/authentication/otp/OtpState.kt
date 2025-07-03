package com.example.mam.viewmodel.authentication.otp

data class OtpState(
    val code: List<String?> = (1..6).map {null},
    val focusedIndex: Int? = null,
)
