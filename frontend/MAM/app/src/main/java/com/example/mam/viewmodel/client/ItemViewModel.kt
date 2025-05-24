package com.example.mam.viewmodel.client

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mam.R
import com.example.mam.entity.CartItem
import com.example.mam.entity.Product
import com.example.mam.entity.Variance
import com.example.mam.entity.VarianceOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ItemViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _item = MutableStateFlow<Product?>(null)
//    val item: StateFlow<Product?> get() = _item
//
//    init {
//        val itemId: String? = savedStateHandle["itemId"]
//        itemId?.let { fetchItem(it) }
//    }
//
//    private fun fetchItem(itemId: String) {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitClient.api.getItemById(itemId)
//                _item.value = response
//            } catch (e: Exception) {
//                // Xử lý lỗi
//            }
//        }
//    }
    val item = Product(
        "P003",
        "Pizza truyền thống",
        "",
        "Sốt BBQ đặc trưng, gà nướng, hành tây, ớt chuông, lá basil và phô mai Mozzarella. ",
        100000,
        true,
        "PC001",
        R.drawable.bacon_and_cheese_heaven)
    private val _cartItem = MutableStateFlow(CartItem(product = item, 1))
    var cartItem = _cartItem.asStateFlow()
    fun loadAllVariance(): List<Variance> {
        return mutableListOf(
            Variance("V001", "Độ dày đế bánh", "P003"),
            Variance("V002", "Độ dày đế bánh", "P004"),
            Variance("V003", "Độ dày đế bánh", "P005"),
            Variance("V004", "Tùy chọn loại bỏ nguyên liệu", "P003"),
            Variance("V005", "Tùy chọn loại bỏ nguyên liệu", "P004"),
            Variance("V006", "Tùy chọn loại bỏ nguyên liệu", "P005"),
            Variance("V007", "Kích cỡ bánh", "P003"),
            Variance("V008", "Kích cỡ bánh", "P004"),
            Variance("V009", "Kích cỡ bánh", "P005")
        )
    }

    fun loadVariance(idProduct: String = item.id): List<Variance>{
        return loadAllVariance().filter {it.idProduct == idProduct}
    }
    fun loadAllOption(): List<VarianceOption>{
        return mutableListOf(
            VarianceOption("V001P003", "V001", "Mỏng", 0),
            VarianceOption("V002P003", "V001", "Thường", 0),
            VarianceOption("V003P003", "V001", "Dày", 0),

            VarianceOption("V001P004", "V002", "Mỏng", 0),
            VarianceOption("V002P004", "V002", "Thường", 0),
            VarianceOption("V003P004", "V002", "Dày", 0),

            VarianceOption("V004P003", "V004", "Hành tây", 0),
            VarianceOption("V005P003", "V004", "Ớt chuông", 0),
            VarianceOption("V006P003", "V004", "Lá Basil", 0),

            VarianceOption("V004P004", "V005", "Hành tây", 0),
            VarianceOption("V005P004", "V005", "Ớt chuông", 0),
            VarianceOption("V006P004", "V005", "Lá Basil", 0),

            VarianceOption("V007P003", "V007", "20cm", 0),
            VarianceOption("V008P003", "V007", "25cm", 0),
            VarianceOption("V009P003", "V007", "30cm", 0),

            VarianceOption("V007P004", "V008", "20cm", 0),
            VarianceOption("V008P004", "V008", "25cm", 0),
            VarianceOption("V009P004", "V008", "30cm", 0),

            VarianceOption("V001P005", "V003", "Mỏng", 0),
            VarianceOption("V002P005", "V003", "Thường", 0),
            VarianceOption("V003P005", "V003", "Dày", 0),

            VarianceOption("V004P005", "V006", "Hành tây", 0),
            VarianceOption("V005P005", "V006", "Ớt chuông", 0),
            VarianceOption("V006P005", "V006", "Lá Basil", 0),

            VarianceOption("V007P005", "V009", "20cm", 0),
            VarianceOption("V008P005", "V009", "25cm", 0),
            VarianceOption("V009P005", "V009", "30cm", 0)
        )
    }

    fun loadOption(idVariance: String): List<VarianceOption>{
        return loadAllOption().filter { it.idVariance == idVariance }
    }

    fun addToCart(){
        //getCart
        //addToCart
        //back
    }

}