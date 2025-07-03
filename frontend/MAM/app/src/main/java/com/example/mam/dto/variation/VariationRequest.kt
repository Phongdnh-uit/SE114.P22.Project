package com.example.mam.dto.variation

data class VariationRequest(
    val name: String = " ",
    val isMultipleChoice: Boolean = false,
    val productId: Long = 0L,
)
