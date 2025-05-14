package com.example.mam.entity

import androidx.compose.ui.graphics.vector.ImageVector
import java.time.Instant

data class Notification(
    val id: String,
    val title: String,
    val content: String,
    val timestamp: String,
    val isRead: Boolean,
    val icon: ImageVector,
    val createAt: Instant = Instant.now(),
    val updateAt: Instant = Instant.now(),
)

