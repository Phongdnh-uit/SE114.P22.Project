package com.example.mam.viewmodel.client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.product.CategoryResponse
import com.example.mam.dto.product.ProductResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _listCategory = MutableStateFlow<MutableList<CategoryResponse>>(mutableListOf())

    private val _recommendedProducts = MutableStateFlow<List<ProductResponse>>(emptyList())
    val recommendedProducts = _recommendedProducts.asStateFlow()

    private val _cartCount = MutableStateFlow<Int>(0)
    val cartCount: StateFlow<Int> = _cartCount

    private val _notificationCount = MutableStateFlow<Int>(0)
    val notificationCount: StateFlow<Int> = _notificationCount

    suspend fun loadCartCount() {
        try {
            val response = BaseRepository(userPreferencesRepository).cartRepository.getMyCartCount()
            Log.d("CartViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                val count = response.body()
                if (count != null) {
                    _cartCount.value = count
                    Log.d("CartViewModel", "Cart count loaded: $count")
                } else {
                    Log.d("CartViewModel", "No cart found")
                }
            } else {
                Log.d("CartViewModel", "Failed to load cart count: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CartViewModel", "Failed to load cart count: ${e.message}")
        }
    }

    suspend fun loadNotificationCount() {
        try {
            val response = BaseRepository(userPreferencesRepository).notificationRepository.countMyUnreadNotifications()
            Log.d("NotificationViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                val count = response.body()
                if (count != null) {
                    _notificationCount.value = count.toInt()
                    Log.d("NotificationViewModel", "Notification count loaded: $count")
                } else {
                    Log.d("NotificationViewModel", "No notifications found")
                }
            } else {
                Log.d("NotificationViewModel", "Failed to load notification count: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("NotificationViewModel", "Failed to load notification count: ${e.message}")
        }
    }

    suspend fun loadAdditionalProduct(){
        try {
            val response = BaseRepository(userPreferencesRepository).productRepository.getRecommendedProducts()
            Log.d("CartViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                val products = response.body()
                if (products != null) {
                    _recommendedProducts.value = products
                    Log.d("CartViewModel", "Recommended products loaded: ${_recommendedProducts.value.size} items")
                } else {
                    Log.d("CartViewModel", "No recommended products found")
                }
            } else {
                Log.d("CartViewModel", "Failed to load recommended products: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CartViewModel", "Failed to load additional products: ${e.message}")
        }
    }

    private val _productMap = MutableStateFlow<Map<Long, List<ProductResponse>>>(emptyMap())
    val productMap: StateFlow<Map<Long, List<ProductResponse>>> = _productMap
    fun getListCategory(): List<CategoryResponse> {
        return _listCategory.value.toList()
    }

    suspend fun loadListCategory(){
        var currentPage = 0
        val allCategories = mutableListOf<CategoryResponse>()
        try {
            Log.d("Category", "Bắt đầu lấy Danh mục")

            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .productCategoryRepository
                    .getCategories(filter = "", page = currentPage)

                Log.d("Category", "Status code: ${response.code()}")

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allCategories.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        Log.d("Category", "Lấy trang ${page.page}")
                        _listCategory.value = allCategories.toMutableList()

                    }
                    else break
                } else {
                    Log.d("Category", "Lấy Danh mục thất bại: ${response.errorBody()}")
                    break // Stop loop on failure
                }
            }

            _listCategory.value = allCategories.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.d("Category", "Không thể lấy Danh mục: ${e.message}")
        }
    }

    fun setAdressAndCoordinates( address: String, longitude: Double, latitude: Double) {
        viewModelScope.launch {
            userPreferencesRepository.saveAddress(address, longitude, latitude)
        }
    }
    suspend fun loadListProduct(id: Long): List<ProductResponse>{
        try {
            Log.d("Category", "Bắt đầu lấy Danh mục")
                val response = BaseRepository(userPreferencesRepository)
                    .productRepository
                    .getProductsByCategory(id,filter= "", page = 0)
                Log.d("Category", "Status code: ${response.code()}")

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        return page.content
                    }
                    else return listOf()
                } else {
                    Log.d("Category", "Lấy Danh mục thất bại: ${response.errorBody()}")
                    return listOf()
                }
        } catch (e: Exception) {
            Log.d("Product", "Không thể lấy Sản phẩm: ${e.message}")
            return listOf()
        }
    }
    suspend fun loadAllProductsFromCategories(categories: List<CategoryResponse>){
        val result = mutableMapOf<Long, List<ProductResponse>>()
        for(category in categories){
            val products = loadListProduct(category.id)
            result[category.id] = products
        }
        _productMap.value = result
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                HomeScreenViewModel(application.userPreferencesRepository)
            }
        }
    }
}