package com.example.mam.dto

import java.time.Instant

open class BaseResponse(
    val id: Long = 0,
    val createdAt: String = "",
    val updatedAt: String = "",
    val createBy: Long = 0,
    val updateBy: Long = 0,
)
