package com.example.mam.entity

import androidx.compose.ui.graphics.vector.ImageVector

data class Notification(
    val id: String,
    val title: String,
    val content: String,
    val timestamp: String,
    val isRead: Boolean,
    val icon: ImageVector
)
