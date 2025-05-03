package com.example.mam.entity

import androidx.annotation.DrawableRes
import java.text.DecimalFormat

data class Product(
    val id: String = "",
    val name: String = "",
    val shortDescription: String = "",
    val longDescription: String = "",
    val originalPrice: Int = 0,
    val isAvailable: Boolean = true,
    val idCategory: String = "",
    @DrawableRes val img: Int = 0,
){
    fun getPriceToString(): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(originalPrice)} VND"
    }
}
