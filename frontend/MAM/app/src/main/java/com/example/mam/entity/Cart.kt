package com.example.mam.entity

import java.text.DecimalFormat

data class Cart (
    val items: MutableList<CartItem> =  mutableListOf(),
    var total: Int = 0
){
    fun addItem(item: CartItem) {
        val existItem = items.find { it == item }
        if (existItem != null) {
            existItem.quantity += item.quantity

        } else {
            items.add(item)
        }
        total += item.getPrice() * item.quantity // Assuming CartItem has a `price` field
    }

    fun delItem(item: CartItem) {
        val removed = items.removeIf { it == item }
        if (removed) {
            total -= item.getPrice() * item.quantity
        }
    }

    fun clearCart(){
        items.clear()
        total = 0
    }

    fun getTotalToString(): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(total)} VND"
    }

}