package com.example.mam.dto.user

import java.io.File

data class UserRequest(
    val fullname: String = "",
    val username: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: File? = null,
    val roleId: Long = 0L,
)
