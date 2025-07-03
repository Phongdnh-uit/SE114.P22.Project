package com.example.mam.dto.product

import okhttp3.MultipartBody
import java.math.BigDecimal

data class ProductRequest(
    val categoryId: Long,
    val name: String,
    val shortDescription: String,
    val detailDescription: String,
    val originalPrice: BigDecimal,
    val image: MultipartBody.Part
)
