package com.example.mam.viewmodel.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Product
import com.example.mam.entity.ProductCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsCategoryViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _isLoadingCategory = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoadingCategory

    private val _isLoadingProduct = MutableStateFlow(false)
    val isLoadingProduct: StateFlow<Boolean> = _isLoadingProduct

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val _category = MutableStateFlow<ProductCategory?>(null)
    val category: StateFlow<ProductCategory?> = _category

    private val _productList = MutableStateFlow<MutableList<Product>>(mutableListOf())
    val productList: StateFlow<List<Product>> = _productList


    fun setCategory(category: ProductCategory) {
        _category.value = category
    }

    fun clearCategory() {
        _category.value = null
    }

    fun setEditMode(isEditMode: Boolean) {
        _isEditMode.value = isEditMode
    }

    fun loadCategory() {
        viewModelScope.launch {
            try {
                _isLoadingCategory.value = true
                // Simulate network call
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoadingCategory.value = false
            }
        }
    }

    fun loadProduct() {
        viewModelScope.launch {
            try {
                _isLoadingProduct.value = true
                // Simulate network call
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoadingProduct.value = false
            }
        }
    }
}