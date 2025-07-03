package com.example.mam.dto.vo

data class PageVO<T>(
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val numberOfElements: Int,
    val content: List<T>
)
