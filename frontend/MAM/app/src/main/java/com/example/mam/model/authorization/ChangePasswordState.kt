package com.example.mam.model.authorization

data class ChangePasswordState(
    val username: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val repeatPassword: String = "",
    val oTP: String = ""
)
