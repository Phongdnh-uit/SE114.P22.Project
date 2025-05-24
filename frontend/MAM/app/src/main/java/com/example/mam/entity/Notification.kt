package com.example.mam.entity

import androidx.compose.ui.graphics.vector.ImageVector
import java.time.Instant

data class Notification(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Instant = Instant.now(),
    val isRead: Boolean = false,
    val icon: ImageVector? = null,
    val createAt: Instant = Instant.now(),
    val updateAt: Instant = Instant.now(),
    val type: String = "",
)

