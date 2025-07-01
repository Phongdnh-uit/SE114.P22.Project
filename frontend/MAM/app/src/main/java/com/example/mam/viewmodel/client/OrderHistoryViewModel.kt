package com.example.mam.viewmodel.client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.Constant
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.order.OrderResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class OrderHistoryViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _orders = MutableStateFlow<List<OrderResponse>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _asc = MutableStateFlow(false)
    val asc: StateFlow<Boolean> = _asc

    private val _orderStaus = MutableStateFlow<List<String>>(listOf())
    val orderStatus = _orderStaus.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun loadOrders(status : String = "") {
        var currentPage = 0
        val allOrders = mutableListOf<OrderResponse>()
        try {
            _isLoading.value = true
            while (true) {
                Log.d("OrderHistoryViewModel", "Loading page $currentPage with status: $status")
                val response = BaseRepository(userPreferencesRepository).orderRepository.getMyOrders(
                    page = currentPage,
                    sort = listOf("createdAt,${if (_asc.value) "asc" else "desc"}"),
                    filter = if (status.isNotBlank()) {
                        "orderStatus ~ '*$status*'"
                    } else {
                        ""
                    },
                )
                Log.d("OrderHistoryViewModel", "Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    val page = response.body()
                    Log.d("OrderHistoryViewModel", "Page content size: ${page?.content?.size ?: 0}")
                    Log.d("OrderHistoryViewModel", "Total pages: ${page?.totalPages ?: 0}")
                    Log.d("OrderHistoryViewModel", "Current page: ${page?.page ?: 0}")
                    if (page != null) {
                        Log.d("OrderHistoryViewModel", "list of orders: ${allOrders.size} orders loaded so far")
                        Log.d("OrderHistoryViewModel", "Adding ${page.content.size} orders to the list")
                        allOrders.addAll(page.content)
                        Log.d("OrderHistoryViewModel", "Total orders loaded: ${allOrders.size}")
                        _orders.value = allOrders
                        if (page.page >= (page.totalPages - 1)) {
                            break // No more pages to load
                        }
                        currentPage++

                        Log.d("OrderHistoryViewModel", "Current orders size: ${_orders.value.size}")
                        for (order in _orders.value) {
                            Log.d("OrderHistoryViewModel", "Order ID: ${order.id}, Status: ${order.orderStatus}")
                        }
                        for (order in page.content) {
                            Log.d("OrderHistoryViewModel", "Order ID: ${order.id}, Status: ${order.orderStatus}")
                        }
                        _isLoading.value = false
                        Log.d(
                            "OrderHistoryViewModel",
                            "Orders loaded successfully: ${_orders.value.size} orders"
                        )
                    }
                } else {
                    Log.e(
                        "OrderHistoryViewModel",
                        "Failed to load orders: ${response.errorBody()?.string()}"
                    )
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("OrderHistoryViewModel", "Failed to load orders: ${e.message}")

        } finally {
            _isLoading.value = false
        }
    }

    suspend fun loadOrderStatus() {
        _isLoading.value = true
        try {
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.getMetadata(
                listOf(Constant.metadata.ORDER_STATUS.name)
            )
            Log.d("OrderViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                _orderStaus.value = response.body()?.get(Constant.metadata.ORDER_STATUS.name) ?: listOf()
                Log.d("OrderViewModel", "Order status loaded successfully: ${_orderStaus.value.size} statuses")
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