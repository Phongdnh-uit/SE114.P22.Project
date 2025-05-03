package com.example.mam.entity

import java.text.DecimalFormat

data class VarianceOption(
    val id: String = "",
    val idVariance: String = "",
    val value: String = "",
    val additionalPrice: Int = 0
){
    fun getPriceToString(): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(additionalPrice)} VND"
    }
}

