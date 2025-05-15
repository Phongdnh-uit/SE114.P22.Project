package com.example.mam.entity

import java.text.DecimalFormat

data class Cart (
    val items: MutableList<CartItem> =  mutableListOf(),
    var total: Int = 0
){
    fun calculateTotal() {
        total = items.sumOf { it.getPrice() * it.quantity }
    }
    fun addItem(item: CartItem) {
        val existItem = items.find { it == item }
        if (existItem != null) {
            existItem.quantity += item.quantity

        } else {
            items.add(item)
        }
        calculateTotal() // Assuming CartItem has a `price` field
    }

    fun delItem(item: CartItem) {
        val removed = items.removeIf { it == item }
        if (removed) {
           calculateTotal()
        }
    }

    fun clearCart(){
        items.clear()
        total = 0
    }

    fun getTotalToString(): String{
        calculateTotal()
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(total)} VND"
    }

    fun changeItem(oldItem: CartItem, newItem: CartItem){
        val remove = items.removeIf { it == oldItem}
        val add = items.add(newItem)
        if (remove && add) calculateTotal()

    }

}