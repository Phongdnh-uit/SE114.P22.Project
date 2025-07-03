package com.example.mam.dto.product

import com.example.mam.dto.BaseResponse
import okhttp3.MultipartBody
import java.io.File

data class CategoryRequest(
    val name: String,
    val description: String,
    val image: File? = null,
)
