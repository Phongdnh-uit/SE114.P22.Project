package com.example.mam.dto.product
import com.example.mam.data.Constant.STORAGE_URL
import com.example.mam.dto.BaseResponse
import java.math.BigDecimal
import java.text.DecimalFormat

class ProductResponse (
    val name: String = "",
    val shortDescription: String = "",
    val detailDescription: String = "",
    val originalPrice: BigDecimal = BigDecimal.ZERO,
    val imageUrl: String = "",
    val isAvailable: Boolean = true,
    val categoryId: Long = 0L,
    val categoryName: String = "",
) : BaseResponse(){
    fun getPriceToString(): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(originalPrice)} VND"
    }

    fun getRealURL(): String {
        return STORAGE_URL + imageUrl.replace("\\", "/")
    }
}
