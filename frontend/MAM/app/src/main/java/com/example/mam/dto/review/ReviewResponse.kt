package com.example.mam.dto.review

import com.example.mam.dto.BaseResponse
import java.time.Instant

data class ReviewResponse(
    val orderId: Long = 0L,
    val rate: Int = 0,
    val content: String? = null,
    val reply: String? = null,
): BaseResponse()
