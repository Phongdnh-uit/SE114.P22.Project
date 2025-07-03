package com.example.mam.dto.user

import com.example.mam.data.Constant.BASE_AVT
import com.example.mam.data.Constant.BASE_URL
import com.example.mam.data.Constant.STORAGE_URL
import com.example.mam.dto.BaseResponse
import com.example.mam.dto.role.RoleResponse


data class UserResponse(
    val fullname: String = "",
    val username: String = "",
    val email: String = "",
    val phone: String = "",
    val avatarUrl: String? = null,
    val status: String = "",
    val loginProvider: String = "",
    val role: RoleResponse = RoleResponse()
): BaseResponse(){
    fun getRealURL(): String {
        if (avatarUrl.isNullOrEmpty()) {
            return BASE_AVT
        }
        return STORAGE_URL + avatarUrl?.replace("\\", "/")
    }
}
