package com.example.mam.entity

import java.time.Instant

data class User(
    val id: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val avatarUrl: String = "",
    val address: String = "",
    val createAt: Instant = Instant.now(),
    val updateAt: Instant = Instant.now(),
) {
}
