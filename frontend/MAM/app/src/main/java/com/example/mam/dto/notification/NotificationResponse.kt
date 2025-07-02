package com.example.mam.dto.notification

import com.example.mam.dto.BaseResponse
import java.time.Instant

data class NotificationResponse(
    val type: String = "",
    val title: String = "",
    val message: String = "",
    val status: Int = 0,
    val userId: Long = 0L,
    val isRead: Boolean = false
): BaseResponse()
