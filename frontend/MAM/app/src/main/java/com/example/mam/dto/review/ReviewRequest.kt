package com.example.mam.dto.review
data class ReviewRequest(
    val orderId: Long,
    val rate: Int,
    val content: String ?= null,
    val reply: String ?= null
)
