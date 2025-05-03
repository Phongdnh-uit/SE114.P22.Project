package com.example.mam.entity

import androidx.annotation.DrawableRes

data class ProductCategory(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    @DrawableRes val  icon: Int
)
