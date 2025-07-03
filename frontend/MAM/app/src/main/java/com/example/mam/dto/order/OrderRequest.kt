package com.example.mam.dto.order

data class OrderRequest(
    val destinationLatitude: Double = 0.0,
    val destinationLongitude: Double = 0.0,
    val shippingAddress: String = "",
    val note: String ?= null,
    val paymentMethod: String = "",
    val promotionId: Long ?= null,
)