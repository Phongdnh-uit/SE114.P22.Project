package com.example.mam.viewmodel.client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.notification.NotificationResponse
import com.example.mam.repository.paging.createPagingFlow
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

open class NotificationViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    @OptIn(ExperimentalCoroutinesApi::class)
    val notifications = _isLoading.flatMapLatest {
        createPagingFlow { page, size, sort, filter ->
            BaseRepository(userPreferencesRepository).notificationRepository.getMyNotifications(
                page = page,
                size = size,
                sort = listOf("createdAt,desc"),
            ).body()?.content ?: emptyList()
        }.cachedIn(viewModelScope)
    }

    suspend fun markAllAsRead() {
        try{
            Log.d("NotificationViewModel", "Marking all notifications as read")
            val response = BaseRepository(userPreferencesRepository).notificationRepository.markAllMyNotificationsAsRead()
            if (response.isSuccessful) {
                Log.d("NotificationViewModel", "All notifications marked as read successfully")
            } else {
                Log.d("NotificationViewModel", "Failed to mark all notifications as read: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("NotificationViewModel", "Error marking all notifications as read: ${e.message}")
        }finally {
            _isLoading.value = !_isLoading.value
        }
    }

    suspend fun markAsRead(id: Long) {
        try {
            Log.d("NotificationViewModel", "Marking notification $id as read")
            val response = BaseRepository(userPreferencesRepository).notificationRepository.markNotificationAsRead(id)
            if (response.isSuccessful) {
                Log.d("NotificationViewModel", "Notification $id marked as read successfully")
            } else {
                Log.d("NotificationViewModel", "Failed to mark notification $id as read: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("NotificationViewModel", "Error marking notification $id as read: ${e.message}")
        } finally {
            _isLoading.value = !_isLoading.value
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