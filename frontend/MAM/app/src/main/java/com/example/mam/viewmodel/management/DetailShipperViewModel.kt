package com.example.mam.viewmodel.management

import androidx.lifecycle.ViewModel
import com.example.mam.entity.Order
import com.example.mam.entity.Shipper
import kotlinx.coroutines.flow.MutableStateFlow

class DetailShipperViewModel(): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> = _isLoading

    private val _shipper = MutableStateFlow<Shipper>(Shipper())
    val shipper: MutableStateFlow<Shipper> = _shipper

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: MutableStateFlow<Boolean> = _isEditMode

    private val _orderList = MutableStateFlow<MutableList<Order>>(mutableListOf())
    val orderList: MutableStateFlow<MutableList<Order>> = _orderList

    fun loadData() {
        _isLoading.value = true
        // Simulate network call
        // After loading data, set isLoading to false
        _isLoading.value = false
    }
}