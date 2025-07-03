package com.example.mam.dto.promotion

import java.math.BigDecimal
import java.time.Instant

data class PromotionRequest(
    val description: String,
    val discountValue: BigDecimal = BigDecimal.ZERO,
    val minValue: BigDecimal = BigDecimal.ZERO,
    val startDate: String,
    val endDate: String,
    val code: String,
    val isPublic: Boolean = true
)
