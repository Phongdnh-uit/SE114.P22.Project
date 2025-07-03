package com.example.mam.dto.promotion

import com.example.mam.dto.BaseResponse
import java.math.BigDecimal

data class PromotionResponse(
    val description: String = "",
    val discountValue: BigDecimal = BigDecimal.ZERO,
    val minValue: BigDecimal = BigDecimal.ZERO,
    val startDate: String = "",
    val endDate: String = "",
    val code: String = "",
): BaseResponse(){
    fun getDiscountAmount(): String {
        val format = java.text.DecimalFormat("#,###")
        return "${format.format(discountValue)} VND"
    }
}
