package com.example.mam.dto.variation

import com.example.mam.dto.BaseResponse

data class VariationResponse(
    val name: String = "",
    val isMultipleChoice: Boolean = false,
    val productId: Long = 0L,
): BaseResponse()
