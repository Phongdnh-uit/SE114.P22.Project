package com.example.mam.viewmodel.client

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.order.OrderResponse
import com.example.mam.dto.review.ReviewRequest
import com.example.mam.dto.review.ReviewResponse
import com.example.mam.dto.shipper.ShipperResponse
import com.example.mam.dto.user.UserResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val orderId = savedStateHandle.get<Long>("orderId") ?: 0L

    private val _order = MutableStateFlow<OrderResponse>(OrderResponse())
    val order = _order.asStateFlow()

    private val _user = MutableStateFlow(UserResponse())
    val user = _user.asStateFlow()

    private val _shipper = MutableStateFlow<ShipperResponse?>(null)
    val shipper = _shipper.asStateFlow()

    private val _review = MutableStateFlow<ReviewResponse?>(null)
    val review = _review.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    suspend fun cancelOrder() :Int {
        try {
            val response = BaseRepository(userPreferencesRepository).orderRepository.cancelOrder(orderId)
            Log.d("OrderViewModel", "Canceling order with ID: $orderId, Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("OrderViewModel", "Order canceled successfully")
                loadOrder()
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

    suspend fun maskAsDeliveried(): Int {
        try {
            val response = BaseRepository(userPreferencesRepository).orderRepository.markOrderAsDelivered(orderId)
            Log.d("OrderViewModel", "Marking order as delivered with ID: $orderId, Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("OrderViewModel", "Order marked as delivered successfully")
                loadOrder()
                return 1 // Successfully marked as delivered
                // Optionally, you can reload the order or update the UI accordingly
            } else {
                Log.d("OrderViewModel", "Failed to mark order as delivered: ${response.errorBody()?.string()}")
                return 0 // Failed to mark as delivered
            }

        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exception
            Log.d("OrderViewModel", "Exception while marking order as delivered: ${e.message}")
            return 0 // Failed due to exception
        }
    }

    suspend fun loadOrder(){
        _isLoading.value = true
        try {
            val response = BaseRepository(userPreferencesRepository).orderRepository.getOrderById(orderId)
            Log.d("OrderViewModel", "Loading order with ID: $orderId, Response Code: ${response.code()}")
            if (response.isSuccessful) {
                _order.value = response.body() ?: OrderResponse()
                Log.d("OrderViewModel", "Order loaded successfully: ${_order.value.orderDetails.size} items")

                val userResponse = BaseRepository(userPreferencesRepository).userRepository.getUserById(_order.value.userId)
                if (userResponse.isSuccessful && userResponse.body() != null) {
                    _user.value = userResponse.body()!!
                } else {
                    Log.d("OrderViewModel", "Failed to load user: ${userResponse.errorBody()?.string()}")
                    _user.value = UserResponse(username = "Unknown User")
                }
                Log.d("OrderViewModel", "User loaded successfully: ${_user.value.username}")


                _shipper.value = _order.value.shipperId?.let {
                    BaseRepository(userPreferencesRepository).shipperRepository.getShipperById(
                        it
                    ).body()
                }

                Log.d("OrderViewModel", "Shipper loaded successfully: ${_shipper.value?.fullname ?: "No shipper assigned"}")


            } else {
                Log.d("OrderViewModel", "Failed to load order: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("OrderViewModel", "Failed to load order: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun loadReview() {
        try {
            val response = BaseRepository(userPreferencesRepository).orderRepository.getReviewByOrderId(orderId)
            Log.d("OrderViewModel", "Loading review for order ID: $orderId, Response Code: ${response.code()}")
            if (response.isSuccessful) {
                _review.value = response.body()
                Log.d("OrderViewModel", "Review loaded successfully")
            } else {
                Log.d("OrderViewModel", "Failed to load review (BE): ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("OrderViewModel", "Failed to load review: ${e.message}")
        } finally {
        }
    }

    suspend fun createReview(review: ReviewRequest): Int {
        try {
            val response = BaseRepository(userPreferencesRepository).orderRepository.createReview(review)
            Log.d("OrderViewModel", "Creating review for order ID: $orderId, Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("OrderViewModel", "Review created successfully")
                loadReview()
                return 1
            } else {
                Log.d("OrderViewModel", "Failed to create review: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("OrderViewModel", "Exception while creating review: ${e.message}")
            return 0
        }
    }

    suspend fun updateReview(reviewId: Long, review: ReviewRequest): Int {
        try {
            val response = BaseRepository(userPreferencesRepository).orderRepository.updateReview(reviewId, review)
            Log.d("OrderViewModel", "Updating review with ID: $reviewId, Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("OrderViewModel", "Review updated successfully")
                loadReview()
                return 1
            } else {
                Log.d("OrderViewModel", "Failed to update review (BE): ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("OrderViewModel", "Exception while updating review: ${e.message}")
            return 0
        }
    }


    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                OrderViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    application.userPreferencesRepository)
            }
        }
    }
}