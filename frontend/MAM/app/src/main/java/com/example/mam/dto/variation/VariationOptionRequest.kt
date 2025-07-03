package com.example.mam.dto.variation

data class VariationOptionRequest(
    val value: String = "",
    val additionalPrice: Double = 0.0,
    val variationId: Long= 0L
)
