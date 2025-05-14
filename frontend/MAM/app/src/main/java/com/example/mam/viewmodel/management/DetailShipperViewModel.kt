package com.example.mam.viewmodel.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Order
import com.example.mam.entity.Shipper
import com.example.mam.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailShipperViewModel(savedStateHandle: SavedStateHandle): ViewModel() {
    private val idShipper = savedStateHandle.get<String>("idShipper") ?: ""

    private val _shipper = MutableStateFlow<Shipper?>(null)
    val shipper = _shipper.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(mutableListOf())
    val orders = _orders.asStateFlow()

    private val _isLoadingOrders = MutableStateFlow(false)
    val isLoadingOrders = _isLoadingOrders.asStateFlow()

    fun loadShipper() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call to load shipper
                // _shipper.value = loadShipperFromNetwork(idShipper)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadOrders() {
        viewModelScope.launch {
            try {
                _isLoadingOrders.value = true
                // Simulate network call to load orders
                // _orders.value = loadOrdersFromNetwork(idShipper)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoadingOrders.value = false
            }
        }
    }

}