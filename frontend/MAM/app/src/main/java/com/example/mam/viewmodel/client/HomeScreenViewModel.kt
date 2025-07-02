package com.example.mam.viewmodel.client

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.product.CategoryResponse
import com.example.mam.dto.product.ProductResponse
import com.example.mam.repository.paging.createPagingFlow
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    val listCategory = createPagingFlow { page, size, sort, filter ->
        BaseRepository(userPreferencesRepository).productCategoryRepository.getCategories(
            page = page,
            size = size,
            filter = ""
        ).body()?.content ?: emptyList()
    }.cachedIn(viewModelScope)

    private val _productsByCategory = mutableStateMapOf<Long, List<ProductResponse>>()
    val productsByCategory: Map<Long, List<ProductResponse>> get() = _productsByCategory

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

    fun setAdressAndCoordinates( address: String, longitude: Double, latitude: Double) {
        viewModelScope.launch {
            userPreferencesRepository.saveAddress(address, longitude, latitude)
        }
    }

    suspend fun getProductsByCategory(categoryId: Long) {
        _productsByCategory[categoryId] ?: run {
            val result = BaseRepository(userPreferencesRepository).productRepository.getProductsByCategory(
                categoryId,
                page = 0,
                size = 100,
                filter = ""
            ).body()?.content ?: emptyList()
            _productsByCategory[categoryId] = result
        }
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