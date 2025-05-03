package com.example.mam.viewmodel.client

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mam.R
import com.example.mam.entity.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel(): ViewModel() {
    private var _listProduct = MutableStateFlow<MutableList<Product>>(mutableListOf())
    val listProduct: StateFlow<List<Product>> get() = _listProduct
    private val _searchText = MutableStateFlow("")
    var searchText = _searchText.asStateFlow()

    private  val _sortOption = MutableStateFlow("Tất cả")
    val sortOption = _sortOption.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory

    fun loadListProduct(){
        _listProduct.value.add(Product(
            "P000",
            "Burger thịt hun khói phô mai",
            "",
            "",
            100000,
            true,
            "PC002",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P001",
            "Burger thịt hun khói phô mai",
            "",
            "",
            100000,
            true,
            "PC002",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P002",
            "Burger thịt hun khói phô mai",
            "",
            "",
            100000,
            true,
            "PC002",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P003",
            "Pizza truyền thống",
            "",
            "",
            100000,
            true,
            "PC001",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P004",
            "Pizza truyền thống",
            "",
            "",
            100000,
            true,
            "PC001",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P005",
            "Pizza truyền thống",
            "",
            "",
            100000,
            true,
            "PC001",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P006",
            "Hotdog truyền thống",
            "",
            "",
            100000,
            true,
            "PC003",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P007",
            "Hotdog truyền thống",
            "",
            "",
            100000,
            false,
            "PC003",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P008",
            "Hotdog truyền thống",
            "",
            "",
            100000,
            true,
            "PC003",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P009",
            "Gá rán",
            "",
            "",
            100000,
            true,
            "PC004",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P010",
            "Gá rán",
            "",
            "",
            100000,
            true,
            "PC004",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P011",
            "Gá rán",
            "",
            "",
            100000,
            true,
            "PC004",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P012",
            "Bít tết áp chảo",
            "",
            "",
            100000,
            true,
            "PC005",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P013",
            "Bít tết áp chảo",
            "",
            "",
            100000,
            true,
            "PC005",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P014",
            "Bít tết áp chảo",
            "",
            "",
            100000,
            true,
            "PC005",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P015",
            "Nước chanh",
            "",
            "",
            100000,
            true,
            "PC006",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P016",
            "Nước chanh",
            "",
            "",
            100000,
            true,
            "PC006",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P017",
            "Nước chanh",
            "",
            "",
            100000,
            true,
            "PC006",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P018",
            "Khoai tây chiên",
            "",
            "",
            100000,
            true,
            "PC007",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P019",
            "Khoai tây chiên",
            "",
            "",
            100000,
            true,
            "PC007",
            R.drawable.bacon_and_cheese_heaven))
        _listProduct.value.add(Product(
            "P020",
            "Khoai tây chiên",
            "",
            "",
            100000,
            true,
            "PC007",
            R.drawable.bacon_and_cheese_heaven))
    }

    fun setSearchText(query: String){
        _searchText.value = query
    }

    fun getSearchHistory(): List<String>{
        return _searchHistory.value.asReversed()
    }

    fun search() {
        val query = _searchText.value.lowercase()
        if (query.isNotBlank() && !_searchHistory.value.contains(query)) {
            _searchHistory.value = (_searchHistory.value + query).takeLast(10) // Keep last 10 searches
        }
        _listProduct.value = _listProduct.value.filter { it.name.lowercase().contains(query) }.toMutableList()
    }

    fun setSortOption(option: String){
        _sortOption.value = option
    }
    fun sortChange(){
        _listProduct.value = when (_sortOption.value) {
            SortOptions.NameIncr.value -> _listProduct.value.sortedBy { it.name }.toMutableList()
            SortOptions.NameDesc.value -> _listProduct.value.sortedByDescending { it.name }.toMutableList()
            SortOptions.PriceIncr.value -> _listProduct.value.sortedBy { it.originalPrice }.toMutableList()
            SortOptions.PriceDesc.value -> _listProduct.value.sortedByDescending { it.originalPrice }.toMutableList()
            SortOptions.All.value -> loadListProduct().let { _listProduct.value }
            else -> _listProduct.value
        }
    }
     fun clear(){
         loadListProduct()
     }

}
enum class SortOptions(val value: String) {
    All("Tất cả"),
    NameIncr("Tên A-Z"),
    NameDesc("Tên Z-A"),
    PriceIncr("Giá thấp nhất"),
    PriceDesc("Giá đắt nhất"),
}