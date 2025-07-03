package com.example.mam.dto.role

import com.example.mam.dto.BaseResponse

data class RoleResponse(
    val name: String = "",
    val description: String = "",
    val active: Boolean = true,
): BaseResponse()
