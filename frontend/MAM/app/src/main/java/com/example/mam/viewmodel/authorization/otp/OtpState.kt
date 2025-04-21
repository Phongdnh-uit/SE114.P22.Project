package com.example.mam.viewmodel.authorization.otp

data class OtpState(
    val code: List<Int?> = (1..4).map {null},
    val focusedIndex: Int? = null,
    val isValid: Boolean? = null
)
