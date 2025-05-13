package com.example.mam.entity

import androidx.annotation.DrawableRes
import java.text.DecimalFormat
import java.time.Instant

data class Product(
    val id: String = "",
    val name: String = "",
    val shortDescription: String = "",
    val longDescription: String = "",
    val originalPrice: Int = 0,
    val isAvailable: Boolean = true,
    val idCategory: String = "",
    @DrawableRes val img: Int = 0,
    val createAt: Instant = Instant.now(),
    val updateAt: Instant = Instant.now(),
){
    fun getPriceToString(): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(originalPrice)} VND"
    }
}
