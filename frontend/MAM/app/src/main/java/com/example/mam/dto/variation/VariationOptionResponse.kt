package com.example.mam.dto.variation

import com.example.mam.dto.BaseResponse

data class VariationOptionResponse(
    val value: String = "",
    val additionalPrice: Double = 0.0,
    val variationId: Long = 0L
): BaseResponse()
