package com.example.mam.viewmodel.management

import android.util.Log
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
import com.example.mam.dto.notification.NotificationResponse
import com.example.mam.dto.order.OrderRequest
import com.example.mam.dto.order.OrderResponse
import com.example.mam.dto.review.ReviewResponse
import com.example.mam.dto.user.UserResponse
import com.example.mam.repository.paging.createPagingFlow
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ListOrderViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isProcessing = MutableStateFlow<Boolean>(false)
    private val _isPreProcessing = MutableStateFlow<Boolean>(false)
    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf(
        "Tất cả",
        "Ngày đặt hàng",
        "Giá tiền",
    ))
    val sortingOptions: StateFlow<List<String>> = _sortingOptions

    private val _asc = MutableStateFlow<Boolean>(false)
    val asc = _asc.asStateFlow()

    private val _orderState = MutableStateFlow(OrderRequest())
    val orderState = _orderState.asStateFlow()

    private val _selectedSortingOption = MutableStateFlow<String>(_sortingOptions.value[0])
    val selectedSortingOption: StateFlow<String> = _selectedSortingOption

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchHistory = MutableStateFlow<List<String>>(mutableListOf())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory
    private val search = MutableStateFlow<String>("")
    private val sort = MutableStateFlow<List<String>>(listOf("id,desc"))
    fun search(){
        search.value = _searchQuery.value
        setSearchHistory(_searchQuery.value)
    }

    fun sort(){
        val sortOption = when(_selectedSortingOption.value){
            "Ngày đặt hàng" -> "createdAt"
            "Giá tiền" -> "totalPrice"
            else -> "id"
        }
        sort.value = if (_asc.value) listOf("$sortOption,asc") else listOf("$sortOption,desc")
    }

    fun setSelectedSortingOption(option: String) {
        _selectedSortingOption.value = option
    }

    fun setSearchHistory(query: String) {
        if (query.isNotBlank() && !_searchHistory.value.contains(query)) {
            val updatedHistory = _searchHistory.value.toMutableList().apply { add(query) }
            _searchHistory.value = updatedHistory.takeLast(10).reversed() // Giữ tối đa 10 lần tìm kiếm
        }
    }

    fun setSearch(query: String) {
        _searchQuery.value = query
    }

    fun setASC(){
        _asc.value = !_asc.value
    }

    fun setOrderState(isProcessing: Boolean = false, isPreProcessing: Boolean = false) {
        _isProcessing.value = isProcessing
        _isPreProcessing.value = isPreProcessing
        Log.d("orderlist", "${_isProcessing.value}, ${_isPreProcessing.value}")
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val orders: Flow<PagingData<OrderResponse>> = combine(
        search,
        sort
    ) { filter, sort ->
        Pair(filter, sort)
    }.flatMapLatest { (filter, sort) ->
        createPagingFlow(
            filter = filter,
            sort = sort
        ) { page, size, s, f ->
            val baseFilter = listOf(
                "orderStatus", "actualDeliveryTime", "shippingAddress", "note",
                "expectedDeliveryTime", "paymentStatus", "totalPrice", "txnRef",
                "user.id", "user.fullname", "user.email", "user.phone",
                "review.rate", "shipper.fullname", "shipper.phone",
                "shipper.licensePlate", "id", "createdAt", "updatedAt"
            ).joinToString(" or ") { "$it ~~ '*$f*'" }
            val filter = when {
                _isProcessing.value -> "orderStatus in ['CONFIRMED','PROCESSING'] and ($baseFilter)"
                _isPreProcessing.value -> "orderStatus ~~ 'PENDING' and ($baseFilter)"
                else -> "($baseFilter)"
            }
            BaseRepository(userPreferencesRepository).orderRepository.getAllOrders(
                filter = filter,
                sort = s,
                page = page,
                size = size
            ).body()?.content ?: emptyList()
        }
    }.cachedIn(viewModelScope)

    suspend fun loadOwnerOfOrder(id: Long): UserResponse {
        try{
            val response = BaseRepository(userPreferencesRepository)
                .userRepository.getUserById(id)
            if (response.isSuccessful) {
                return response.body() ?: UserResponse()
            } else {
                Log.e("ListOrderViewModel", "Error loading owner of order: ${response.errorBody()?.string()}")
                return UserResponse()
            }
        } catch (e: Exception) {
            Log.e("ListOrderViewModel", "Exception loading owner of order: ${e.message}")
            return UserResponse()
        }
    }

    suspend fun loadReviewOfOrder(id: Long): ReviewResponse{
        try {
            val response = BaseRepository(userPreferencesRepository)
                .orderRepository.getReviewByOrderId(id)
            if (response.isSuccessful) {
                return response.body() ?: ReviewResponse()
            } else {
                Log.e("ListOrderViewModel", "Error loading review of order: ${response.errorBody()?.string()}")
                return  ReviewResponse()
            }
        } catch (e: Exception) {
            Log.e("ListOrderViewModel", "Exception loading review of order: ${e.message}")
            return ReviewResponse()
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ListOrderViewModel(application.userPreferencesRepository)
            }
        }
    }
}