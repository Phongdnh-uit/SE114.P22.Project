package com.example.mam.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AuthEventManager {
    private val _unauthorizedEvent = MutableStateFlow(false)
    val unauthorizedEvent: StateFlow<Boolean> = _unauthorizedEvent

    fun triggerUnauthorized() {
        _unauthorizedEvent.value = true
    }
    fun resetUnauthorized() {
        _unauthorizedEvent.value = false
    }
}