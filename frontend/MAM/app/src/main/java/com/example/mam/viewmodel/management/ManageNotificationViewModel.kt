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
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.notification.NotificationRequest
import com.example.mam.entity.User
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class ManageNotificationViewModel(
    savedStateHandle: SavedStateHandle?,
    private val userPreferencesRepository: UserPreferencesRepository,
):  ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _typeList = MutableStateFlow(listOf("GENERAL",              // Thông báo chung
        "ORDER_PLACED",         // Đơn hàng đã được đặt
        "ORDER_RECEIVED",       // Đơn hàng đã được tiếp nhận
        "ORDER_PREPARING",      // Đơn hàng đang được chế biến
        "ORDER_DELIVERING",     // Đơn hàng đang được giao
        "ORDER_DELIVERED",      // Đơn hàng đã giao xong
        "PROMOTION" ))
    val typeList = _typeList.asStateFlow()

    private val _type = MutableStateFlow(typeList.value[0])
    val type = _type.asStateFlow()

    private val _userList = MutableStateFlow(listOf<User>())
    val userList = _userList.asStateFlow()

    private val _receiverList = MutableStateFlow(listOf<String>())
    val receiverList = _receiverList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun setTitle(title: String) {
        _title.value = title
    }
    fun setMessage(message: String) {
        _message.value = message
    }
    fun setReceiverList(receiverList: List<String>) {
        _receiverList.value = receiverList
    }
    fun setType(type: String) {
        _type.value = type
    }

    suspend fun createNotification(): Int {
        _isLoading.value = true
        try {
            Log.d("Notification", "Bắt đầu them Thong bao")
            Log.d(
                "Notification",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val request  = NotificationRequest(
                type = _type.value,
                title = _title.value,
                message = _message.value,
                status = 0
            )

            val response = BaseRepository(userPreferencesRepository)
                .notificationRepository
                .sendNotificationToAll(request)

            if (response == null){
                return 0
            }
            Log.d("Notification", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val notification = response.body()
                if (notification != null) {
                    _title.value = notification.title
                    _message.value = notification.message
                    _type.value = notification.type
                }
                return 1
            } else {
                Log.d("Notification", "Them thong bao thất bại: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Notification", "Không thể them thong bao: ${e.message}")
            return 0
        } finally {
            _isLoading.value = false
            Log.d("Notification", "Kết thúc them thong bao")
        }
    }

//    fun sendNotification() {
//        // Simulate sending notification
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                // Simulate network call
//                println("Notification sent to ${_receiverList.value.joinToString(", ")}")
//            } catch (e: Exception) {
//                // Handle error
//            } finally {
//                // Hide loading indicator
//                _isLoading.value = false
//            }
//        }
//    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                ManageNotificationViewModel(
                    savedStateHandle = savedStateHandle,
                    userPreferencesRepository = application.userPreferencesRepository,
                )
            }
        }
    }
}