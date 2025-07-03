package com.example.mam.viewmodel.client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mam.MAMApplication
import com.example.mam.data.Constant
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.order.OrderResponse
import com.example.mam.repository.paging.createPagingFlow
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest


class OrderHistoryViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _asc = MutableStateFlow(false)
    val asc: StateFlow<Boolean> = _asc

    private val _orderStatus = MutableStateFlow<List<String>>(listOf())
    val orderStatus = _orderStatus.asStateFlow()

    private val _selectedOrderStatus = MutableStateFlow<String>("")
    val selectedOrderStatus: StateFlow<String> = _selectedOrderStatus

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setOrderStatus(status: String) {
        _selectedOrderStatus.value = status
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val orders: Flow<PagingData<OrderResponse>> = combine(
        _selectedOrderStatus
    ){}.flatMapLatest {
        createPagingFlow{ page, size, s, f ->
            BaseRepository(userPreferencesRepository).orderRepository.getMyOrders(
                filter = "orderStatus ~~ '${_selectedOrderStatus.value}'",
                sort = listOf("createdAt,desc"),
                page = page,
                size = size
            ).body()?.content ?: emptyList()
        }
    }.cachedIn(viewModelScope)

    suspend fun loadOrderStatus() {
        _isLoading.value = true
        try {
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.getMetadata(
                listOf(Constant.metadata.ORDER_STATUS.name)
            )
            Log.d("OrderViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                _orderStatus.value = response.body()?.get(Constant.metadata.ORDER_STATUS.name) ?: listOf()
                Log.d("OrderViewModel", "Order status loaded successfully: ${_orderStatus.value.size} statuses")
            } else {
                Log.d("OrderViewModel", "Failed to load order status: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("OrderViewModel", "Failed to load order status: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                OrderHistoryViewModel(application.userPreferencesRepository)
            }
        }
    }
}