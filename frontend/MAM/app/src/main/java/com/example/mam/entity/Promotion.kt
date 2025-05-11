package com.example.mam.entity

import java.text.DecimalFormat

data class Promotion(
    val code: String = "",
    val value: Int = 0,
){
    fun getValueToString(): String {
        val formatter = DecimalFormat("#,###")
        return "- ${formatter.format(value)} VND"
    }
}
