package com.example.mam.viewmodel.client

import androidx.lifecycle.ViewModel
import com.example.mam.R
import com.example.mam.entity.Cart
import com.example.mam.entity.CartItem
import com.example.mam.entity.Product
import com.example.mam.entity.VarianceOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel(): ViewModel() {
    private val _cart = MutableStateFlow(Cart())
    val cart = _cart.asStateFlow()
    fun getCart(id: String){
        val newCart = Cart()
        val product1 = Product("P003", "Pizza truyền thống", "", "", 120000, true, "PC001", R.drawable.bacon_and_cheese_heaven)
        val product2 = Product("P006", "Hotdog truyền thống", "", "", 80000, true, "PC003", R.drawable.bacon_and_cheese_heaven)

        val option1 = VarianceOption("V007P003", "V007", "25cm", 10000)
        val option2 = VarianceOption("V004P003", "V004", "Hành tây", 5000)
        val option3 = VarianceOption("V008P003", "V007", "30cm", 15000)

        newCart.addItem(CartItem(product1, quantity = 2, options = mutableListOf(option1, option2)))
        newCart.addItem(CartItem(product2, quantity = 1, options = mutableListOf(option3)))

        _cart.value = newCart
    }

    fun loadItem(): MutableList<CartItem>{
        return _cart.value.items
    }

    fun loadAdditionalProduct(): MutableList<Product>{
        return mutableListOf(
            Product(
                "P018",
                "Khoai tây chiên",
                "",
                "",
                100000,
                true,
                "PC007",
                R.drawable.bacon_and_cheese_heaven),
            Product(
                "P019",
                "Khoai tây chiên",
                "",
                "",
                100000,
                true,
                "PC007",
                R.drawable.bacon_and_cheese_heaven),
            Product(
                "P019",
                "Khoai tây chiên",
                "",
                "",
                100000,
                true,
                "PC007",
                R.drawable.bacon_and_cheese_heaven),
            Product(
                "P019",
                "Khoai tây chiên",
                "",
                "",
                100000,
                true,
                "PC007",
                R.drawable.bacon_and_cheese_heaven),
            Product(
                "P020",
                "Khoai tây chiên",
                "",
                "",
                100000,
                true,
                "PC007",
                R.drawable.bacon_and_cheese_heaven)
        )
    }

    fun incrItemQuantity(index: Int){
        val updatedItems = _cart.value.items.toMutableList()
        updatedItems[index] = updatedItems[index].copy(quantity = updatedItems[index].quantity + 1)

        _cart.value = _cart.value.copy(items = updatedItems)
    }

    fun descItemQuantity(index: Int){
        val updatedItems = _cart.value.items.toMutableList()
        updatedItems[index] = updatedItems[index].copy(quantity = if(updatedItems[index].quantity > 1) updatedItems[index].quantity - 1 else 1)
        _cart.value = _cart.value.copy(items = updatedItems)
    }

    fun deleteItem(index: Int){
        val updatedCart = _cart.value.copy() // Tạo một bản sao mới của giỏ hàng hiện tại
        updatedCart.delItem(updatedCart.items[index]) // Xóa item khỏi bản sao
        _cart.value = updatedCart
    }

    fun checkOut(): String{
        //tao Order moi
        _cart.value.clearCart()
        return "" //Ma oder
    }
}