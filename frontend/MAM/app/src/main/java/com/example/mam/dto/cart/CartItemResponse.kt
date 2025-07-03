package com.example.mam.dto.cart

import com.example.mam.data.Constant.STORAGE_URL
import com.example.mam.dto.BaseResponse
import java.math.BigDecimal
import java.text.DecimalFormat

data class CartItemResponse(
    val cartId: Long = 0L,
    val productId: Long = 0L,
    val productName: String = "",
    val imageUrl: String = "",
    var quantity: Long = 0L,
    val price: BigDecimal = BigDecimal.ZERO,
    val variationOptionIds: Set<Long> = emptySet(),
    val variationOptionInfo: String? = "",
    val available : Boolean,
): BaseResponse() {
    fun getPrice(): String {
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(price)} VND"
    }
    fun getRealUrl(): String {
        return STORAGE_URL + imageUrl.replace("\\", "/")
    }
}
