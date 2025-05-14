package com.example.mam.entity

import androidx.annotation.DrawableRes
import java.time.Instant

data class ProductCategory(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    @DrawableRes val  icon: Int,
    val createAt: Instant = Instant.now(),
    val updateAt: Instant = Instant.now(),
)
