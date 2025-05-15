package com.example.mam.entity

import java.text.DecimalFormat

data class OrderItem (
    val product: Product = Product(),
    var quantity: Int = 0,
    val options: String = "",
    val price: Int = 0,
){
    fun getPriceToString(): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(price)} VND"
    }
}