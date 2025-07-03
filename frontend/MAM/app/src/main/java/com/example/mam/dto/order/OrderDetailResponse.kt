package com.example.mam.dto.order

import com.example.mam.data.Constant.STORAGE_URL
import java.math.BigDecimal
import java.text.DecimalFormat

data class OrderDetailResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val productImage: String,
    val variationInfo: String,
    val quantity: Long,
    val price: BigDecimal,
    val totalPrice: BigDecimal,
    val orderId: Long
){
    fun getPrice(): String {
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(totalPrice)} VND"
    }
    fun getRealUrl(): String {
        return STORAGE_URL + productImage.replace("\\", "/")
    }
}
