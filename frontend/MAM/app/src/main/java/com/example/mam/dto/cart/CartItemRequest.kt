package com.example.mam.dto.cart

import java.math.BigDecimal

data class CartItemRequest(
    val productId: Long = 0L,
    val quantity: Long = 0L,
    val variationOptionIds: Set<Long> = emptySet()

)
