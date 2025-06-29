package com.example.mam.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.notification.NotificationResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class NotificationViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _notifications = MutableStateFlow<MutableList<NotificationResponse>>(mutableListOf())
    val notifications = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun loadNotifications() {
        var currentPage = 0
        _isLoading.value = true
        val allNotifications = mutableListOf<NotificationResponse>()
        try {
            Log.d("NotificationViewModel", "Loading notifications")
            while (true) { // Loop until the last page
                Log.d("NotificationViewModel", "Fetching page $currentPage")
                val response = BaseRepository(userPreferencesRepository).notificationRepository.getMyNotifications(
                    page = currentPage,
                    sort = listOf("createdAt,desc"), // Sort by created date descending
                    filter = "")
                Log.d("NotificationViewModel", "Status code: ${response.code()}")
                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allNotifications.addAll(page.content)
                        Log.d("NotificationViewModel", "Đã lấy ${page.content.size} thông báo từ trang ${page.page}")
                        Log.d("NotificationViewModel", "Tổng số thông báo: ${page.totalElements}, Tổng trang: ${page.totalPages}")
                        _notifications.value = allNotifications.toMutableList()// Update UI with new notifications
                        Log.d("NotificationViewModel", "Cập nhật thông báo: ${_notifications.value.size} thông báo hiện có")
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page


                    }
                    else break
                } else {
                    Log.d("NotificationViewModel", "Tai thong bao thất bại: ${response.errorBody()?.string()}")
                    break // Stop loop on failure
                }
            }
        } catch (e: Exception) {
            Log.d("NotificationViewModel", "Không thể lấy thông báo: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }
    suspend fun markAllAsRead() {
        try{
            Log.d("NotificationViewModel", "Marking all notifications as read")
            val response = BaseRepository(userPreferencesRepository).notificationRepository.markAllMyNotificationsAsRead()
            if (response.isSuccessful) {
                Log.d("NotificationViewModel", "All notifications marked as read successfully")
                // Optionally, reload notifications to reflect changes
                loadNotifications()
            } else {
                Log.d("NotificationViewModel", "Failed to mark all notifications as read: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("NotificationViewModel", "Error marking all notifications as read: ${e.message}")
        }
    }

    suspend fun markAsRead(id: Long) {
        try {
            Log.d("NotificationViewModel", "Marking notification $id as read")
            val response = BaseRepository(userPreferencesRepository).notificationRepository.markNotificationAsRead(id)
            if (response.isSuccessful) {
                Log.d("NotificationViewModel", "Notification $id marked as read successfully")
                // Optionally, reload notifications to reflect changes
                loadNotifications()
            } else {
                Log.d("NotificationViewModel", "Failed to mark notification $id as read: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("NotificationViewModel", "Error marking notification $id as read: ${e.message}")
        }

    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                NotificationViewModel(application.userPreferencesRepository)
            }
        }
    }
}