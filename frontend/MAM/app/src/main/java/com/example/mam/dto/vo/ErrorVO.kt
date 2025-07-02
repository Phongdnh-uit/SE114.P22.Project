package com.example.mam.dto.vo
import com.google.gson.Gson

enum class ErrorType(var message: String) {
    VALIDATION_ERROR("Validation error occurred"),
    RESOURCE_NOT_FOUND_ERROR("Resource not found"),
    RESOURCE_CONFLICT_ERROR("Resource conflict occurred"),
    SERVER_ERROR("Internal server error"),
    BAD_REQUEST_ERROR("Bad request");
}
data class ErrorVO(
    val type: ErrorType,
    val details: Map<String, String> = emptyMap()
)

fun HandleError(errorBody: String?): String{
    try {
        val errorVO = Gson().fromJson(errorBody, ErrorVO::class.java)
        if (errorVO.type == ErrorType.VALIDATION_ERROR) {
            val details = errorVO.details.keys.joinToString(", ") {
                when(it)
                {
                    "fullname" -> "họ tên"
                    "phone" -> "số điện thoại"
                    "email" -> "email"
                    "username" -> "tên đăng nhập"
                    "password" -> "mật khẩu"
                    "credentialId" -> "tài khoản đăng nhập"
                    else -> ""
                }
            }
            if (details.replace(",", "").isEmpty()) {
                return "Dữ liệu không hợp lệ"
            }
            return "Lỗi $details không hợp lệ"
        }
        if (errorVO.type == ErrorType.RESOURCE_NOT_FOUND_ERROR) {
            return "Không tìm thấy tài nguyên"
        }
        if (errorVO.type == ErrorType.RESOURCE_CONFLICT_ERROR) {
            val details = errorVO.details.keys.joinToString(", ") {
                when(it)
                {
                    "fullname" -> "họ tên"
                    "phone" -> "số điện thoại"
                    "email" -> "email"
                    "username" -> "tên đăng nhập"
                    else -> ""
                }
            }
            if (details.replace(",", "").isEmpty()) {
                return "Dữ liệu đã tồn tại"
            }
            return "Lỗi $details đã tồn tại"
        }
        if (errorVO.type == ErrorType.SERVER_ERROR) {
            return "Lỗi máy chủ"
        }
        if (errorVO.type == ErrorType.BAD_REQUEST_ERROR) {
            return "Yêu cầu không hợp lệ"
        }
        return "Lỗi không xác định"
    } catch (e: Exception) {
        return "Lỗi không xác định"
    }
}
