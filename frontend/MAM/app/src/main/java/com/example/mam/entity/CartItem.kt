package com.example.mam.entity

import java.text.DecimalFormat

data class CartItem(
    val product: Product = Product(),
    var quantity: Int = 0,
    val options: MutableList<VarianceOption> = mutableListOf()
){
    fun getPrice(): Int{
        var price = product.originalPrice
        options.forEach{ option ->
            price += option.additionalPrice
        }
        return price
    }

    fun getPriceToString(): String{
        val price = this.getPrice()
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(price)} VND"
    }

    fun getOptionsToString(): String{
        return options.joinToString(", ") { it.value }
    }
}
