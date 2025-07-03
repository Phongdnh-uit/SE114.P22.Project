package com.example.mam.dto.cart

import java.text.DecimalFormat

data class CartResponse(
    val id: Long = 0L,
    var cartItems: List<CartItemResponse> = emptyList(),
){
    fun getTotalPrice(): String {
        val total = cartItems.sumOf { it.price * it.quantity.toBigDecimal() }
        val format = DecimalFormat("#,###")
        return "${format.format(total)} VND"
    }
}