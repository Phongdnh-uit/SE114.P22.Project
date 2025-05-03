package com.example.mam.viewmodel.client

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.mam.R
import com.example.mam.entity.Product
import com.example.mam.entity.ProductCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeScreenViewModel(): ViewModel() {
    private val _listCategory = MutableStateFlow<MutableList<ProductCategory>>(mutableListOf())
    private val _listProduct = MutableStateFlow<MutableList<Product>>(mutableListOf())

    fun getListCategory(): List<ProductCategory> {
        return _listCategory.value.toList()
    }

    fun getListProduct(idCategory: String): List<Product> {
        var listProduct: MutableList<Product> = mutableListOf()
        for(product in _listProduct.value.toList()){
            if (product.idCategory.equals(idCategory)) listProduct.add(product)
        }
        return listProduct
    }

    fun loadListCategory(){
        _listCategory.value.add(ProductCategory("PC000", "Đề xuất", "", R.drawable.ic_glowing_star))
        _listCategory.value.add(ProductCategory("PC001", "Pizza", "", R.drawable.ic_pizza))
        _listCategory.value.add(ProductCategory("PC002", "Burger", "", R.drawable.ic_hamburger))
        _listCategory.value.add(ProductCategory("PC003", "Hotdog", "", R.drawable.ic_hotdog))
        _listCategory.value.add(ProductCategory("PC004", "Gà rán", "", R.drawable.ic_chicken))
        _listCategory.value.add(ProductCategory("PC005", "Thịt", "", R.drawable.ic_meat))
        _listCategory.value.add(ProductCategory("PC006", "Thức uống", "", R.drawable.ic_drink))
        _listCategory.value.add(ProductCategory("PC007", "Khác", "", R.drawable.ic_other))
    }

    fun clearListCategory(){
        _listCategory.value.clear()
    }

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

    fun clearListProduct(){
        _listProduct.value.clear()
    }
}