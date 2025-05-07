package com.example.mam.entity

data class Notification(
    val id: String,
    val title: String,
    val content: String,
    val timestamp: String,
    val isRead: Boolean
)
