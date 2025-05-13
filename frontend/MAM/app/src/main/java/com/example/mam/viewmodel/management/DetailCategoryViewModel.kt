package com.example.mam.viewmodel.management

import android.content.SyncAdapterType
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Product
import com.example.mam.entity.ProductCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailCategoryViewModel(savedStateHandle: SavedStateHandle): ViewModel() {
    private val idCategory = savedStateHandle.get<String>("idCategory") ?: ""

    private val _category = MutableStateFlow<ProductCategory?>(null)
    val category: StateFlow<ProductCategory?> = _category

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _products = MutableStateFlow<List<Product>>(mutableListOf())
    val products: StateFlow<List<Product>> = _products

    private val _isLoadingProducts = MutableStateFlow(false)
    val isLoadingProducts: StateFlow<Boolean> = _isLoadingProducts

    fun loadCategory() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call to load category
                // _category.value = loadCategoryFromNetwork(idCategory)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun loadProducts() {
        viewModelScope.launch {
            try {
                _isLoadingProducts.value = true
                // Simulate network call to load products
                // _products.value = loadProductsFromNetwork(idCategory)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoadingProducts.value = false
            }
        }
    }
}