package com.example.mam.viewmodel.authorization

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Notification
import com.example.mam.services.APIservice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

open class NotificationViewModel(
    private val api: APIservice
): ViewModel() {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadNotifications() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = api.getNotifications()
                _notifications.value = response
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun markAllAsRead() {
        _notifications.value = _notifications.value.map {
            it.copy(isRead = true)
        }
    }
}