package com.example.mam.entity

import java.text.DecimalFormat
import java.time.Instant

data class Order(
    val id: String = "",
    val usedId: String = "",
    val orderDate: Instant ?= null,
    val paymentId: String = "",
    val shippingAddress: String = "",
    val orderItems: MutableList<OrderItem> =  mutableListOf(),
    var totalPrice: Int = 0,
    val note: String = "",
    val orderStatus: Int = 0,
    val expectDeliveryTime: Instant ?= null,
    val actualDeliveryTime: Instant?= null,
    val shipperId: String ?= null,
){
    fun getTotalToString(): String{
        calculateTotal()
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(totalPrice)} VND"
    }
    fun calculateTotal() {
        totalPrice = orderItems.sumOf { it.price}
    }
}
