package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.Constant
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.order.OrderResponse
import com.example.mam.dto.review.ReviewResponse
import com.example.mam.dto.shipper.ShipperResponse
import com.example.mam.dto.user.UserResponse
import com.example.mam.repository.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class ManageOrderViewModel(
    savedStateHandle: SavedStateHandle?,
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {
    private val orderId: Long? = savedStateHandle?.get<Long>("orderId")

    private val _orderID = MutableStateFlow<Long>(orderId?: 0L)
    val orderID = _orderID.asStateFlow()

    private val _orderStatuses = MutableStateFlow<List<String>>(listOf())
    val orderStatuses = _orderStatuses.asStateFlow()

    private val _orderStatus = MutableStateFlow<String>("")
    val orderStatus = _orderStatus.asStateFlow()

    private val _shipper = MutableStateFlow<ShipperResponse?>(null)
    val shipper = _shipper.asStateFlow()

    private val _user = MutableStateFlow<UserResponse>(UserResponse())
    val user = _user.asStateFlow()

    private val _order = MutableStateFlow<OrderResponse>(OrderResponse())
    val order = _order.asStateFlow()

    private val _review = MutableStateFlow<ReviewResponse?>(null)
    val review = _review.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isStatusLoading = MutableStateFlow(false)
    val isStatusLoading = _isStatusLoading.asStateFlow()

    fun getNextStatus(): String {
        val values = _orderStatuses.value
        val current = values.indexOfFirst { it == _orderStatus.value }
        return if (current in 0 until values.lastIndex) {
            values[current + 1]
        } else {
            values.firstOrNull() ?: ""
        }
    }

    suspend fun updateStatus(): Int {
        _isLoading.value = true
        try {
            val nextStatus = getNextStatus()
            Log.d("Order", "Bắt đầu cap nhat Don hang")
            Log.d(
                "Order",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response = BaseRepository(userPreferencesRepository)
                .orderRepository
                .getOrderStatus(_orderID.value, nextStatus)
            Log.d("Order", "${_orderID.value}, ${_orderStatus.value}")

            Log.d("Order", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                loadData()
                return 1
            } else {
                Log.d("Order", "Cap nhat Don hang thất bại: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Order", "Không thể cap nhat Don hang: ${e.message}")
            return 0
        } finally {
            _isLoading.value = false
            Log.d("Order", "Kết thúc cap nhat Don hang")
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
                _orderStatuses.value = response.body()?.get(Constant.metadata.ORDER_STATUS.name) ?: listOf()
                Log.d("OrderViewModel", "Order status loaded successfully: ${_orderStatuses.value.size} statuses")
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

    suspend fun loadData() {
        _isLoading.value = true
        try {
            Log.d("Order", "Bắt đầu lấy Don hang")
            Log.d(
                "Order",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            Log.d("Order", "${_orderID.value}")
            val response =
                BaseRepository(userPreferencesRepository).orderRepository.getOrderById(_orderID.value)
            Log.d("Order", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val order = response.body()
                if (order != null) {
                    _order.value = order
                    _orderStatus.value = order.orderStatus
                    Log.d("Order", "Lấy Don hang thành công: ${order.orderStatus}")
                    Log.d("Order", "Lấy Don hang thành công: ${order.orderDetails.size} items")
                    Log.d("Order", "Lấy Don hang thành công: ${order.createdAt}, ${order.note}, ${order.paymentMethod}, ${order.shipperId}")


                    val user = BaseRepository(userPreferencesRepository)
                        .userRepository
                        .getUserById(order.userId)
                    if (user.isSuccessful) {
                        _user.value = user.body() ?: UserResponse()
                        Log.d("Order", "Lấy thông tin người dùng: ${user.body()?.fullname}, $")
                    } else {
                        Log.d("Order", "Lấy thông tin người dùng thất bại: ${user.errorBody()?.string()}")
                    }
                    if (order.shipperId != null) {
                        val shipperResponse = BaseRepository(userPreferencesRepository)
                            .shipperRepository
                            .getShipperById(order.shipperId)
                        if (shipperResponse.isSuccessful) {
                            _shipper.value = shipperResponse.body()
                            Log.d("Order", "Lấy thông tin Shipper: ${shipper.value?.fullname}")
                        } else {
                            Log.d("Order", "Lấy thông tin Shipper thất bại: ${shipperResponse.errorBody()?.string()}")
                        }
                    } else {
                        _shipper.value = null
                    }
                }

            } else {
                Log.d("Order", "Lấy Don hang thất bại: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("Order", "Không thể lấy Don hang: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Order", "Kết thúc lấy Don hang")
        }

    }


    suspend fun cancelOrder() :Int {
        try {
            val response = BaseRepository(userPreferencesRepository).orderRepository.cancelOrder(_orderID.value)
            Log.d("OrderViewModel", "Canceling order with ID: $orderId, Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("OrderViewModel", "Order canceled successfully")
                loadData()
                return 1
            } else {
                Log.d("OrderViewModel", "Failed to cancel order: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("OrderViewModel", "Exception while canceling order: ${e.message}")
            return 0
            // Handle exception
        }
    }


    suspend fun loadReview() {
        try {
            Log.d("Order", "Bắt đầu lấy đánh giá")
            Log.d(
                "Order",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            Log.d("Order", "${_orderID.value}")
            val response =
                BaseRepository(userPreferencesRepository).orderRepository.getReviewByOrderId(_orderID.value)
            Log.d("Order", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val review = response.body()
                if (review != null) {
                    _review.value = review
                    Log.d("Order", "Lấy đánh giá thành công: ${review.rate}, ${review.content}")
                } else {
                    Log.d("Order", "Không có đánh giá cho đơn hàng này")
                }
            } else {
                Log.d("Order", "Lấy đánh giá thất bại: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("Order", "Không thể lấy đánh giá: ${e.message}")
        } finally {
            Log.d("Order", "Kết thúc lấy đánh giá")
        }
    }

    suspend fun reply(content: String) : Int {
        try {
            Log.d("Order", "Bắt đầu trả lời đánh giá")
            Log.d(
                "Order",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response = _review.value?.let {
                BaseRepository(userPreferencesRepository)
                    .orderRepository
                    .replyToReview(it.id, content)
            }
            if (response != null) {
                Log.d("Order", "Status code: ${response.code()}")
            }
            if (response != null) {
                if (response.isSuccessful) {
                    loadReview()
                    return 1
                } else {
                    Log.d("Order", "Trả lời đánh giá thất bại: ${response.errorBody()?.string()}")
                    return 0
                }
            }
        } catch (e: Exception) {
            Log.d("Order", "Không thể trả lời đánh giá: ${e.message}")
            return 0
        }
        finally {
            Log.d("Order", "Kết thúc trả lời đánh giá")
        }
        return 0
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                ManageOrderViewModel(
                    savedStateHandle = savedStateHandle,
                    userPreferencesRepository = application.userPreferencesRepository,
                )
            }
        }
    }

}