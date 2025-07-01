package com.example.mam.dto.order

import com.example.mam.dto.BaseResponse
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.Instant

data class OrderResponse(
    val shippingAddress: String = "",
    val totalPrice: BigDecimal = BigDecimal.ZERO,
    val note: String? = null,
    val expectedDeliveryTime: String = Instant.now().toString(),
    val actualDeliveryTime: String = Instant.now().toString(),
    val orderStatus: String = "",
    val userId: Long = 0L,
    val shipperId: Long? = null,
    val paymentMethod: String = "",
    val paymentStatus: String = "",
    val txnRef: String = "",
    val orderDetails: List<OrderDetailResponse> = emptyList(),
): BaseResponse(){
    fun getPriceToString(): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(totalPrice.toLong())} VND"
    }
}
