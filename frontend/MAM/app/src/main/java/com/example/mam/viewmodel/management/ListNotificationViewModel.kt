package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.notification.NotificationResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListNotificationViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notiList = MutableStateFlow<MutableList<NotificationResponse>>(mutableListOf())
    val notiList: StateFlow<List<NotificationResponse>> = _notiList

    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf(
        "Tất cả",
        "Ngày"
    ))
    val sortingOptions: StateFlow<List<String>> = _sortingOptions

    private val _selectedSortingOption = MutableStateFlow<String>(_sortingOptions.value[0])
    val selectedSortingOption: StateFlow<String> = _selectedSortingOption

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchHistory = MutableStateFlow<List<String>>(mutableListOf())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    private val _desc = MutableStateFlow(true)
    val desc = _desc.asStateFlow()

    //update search history if search query is not blank and not in history
    fun setSearchHistory(query: String) {
        if (query.isNotBlank() && !_searchHistory.value.contains(query)) {
            val updatedHistory = _searchHistory.value.toMutableList().apply { add(query) }
            _searchHistory.value = updatedHistory.takeLast(10).reversed() // Giữ tối đa 10 lần tìm kiếm
        }
    }

    fun setSearch(query: String) {
        _searchQuery.value = query
    }

    suspend fun searchNotification() {
        //search and save history
        _isLoading.value = true
        var currentPage = 0
        val allNotifications = mutableListOf<NotificationResponse>()

        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .notificationRepository.getAllNotifications(filter = "type ~~ '*${_searchQuery.value}*' or title ~~ '*${_searchQuery.value}*' or message ~~ '*${_searchQuery.value}*'", page = currentPage)

                if (response.isSuccessful) {
                    setSearchHistory(_searchQuery.value)
                    val page = response.body()
                    if (page != null){
                        allNotifications.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        Log.d("Category", "Lấy trang ${page.page}")
                        _notiList.value = allNotifications.toMutableList()

                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }

            _notiList.value = allNotifications.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun sortNotification(){
        _isLoading.value = true
        var currentPage = 0
        val allNotifications = mutableListOf<NotificationResponse>()
        val sortOption = when(_selectedSortingOption.value){
            "Ngày" -> "created_at"
            else -> "id"
        }
        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .notificationRepository.getAllNotifications(
                        filter = "",
                        page = currentPage,
                        sort = listOf("${sortOption}," + if (_desc.value) "desc" else "asc"))

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allNotifications.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _notiList.value = allNotifications.toMutableList()

                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }

            _notiList.value = allNotifications.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }


    fun setSelectedSortingOption(option: String) {
        _selectedSortingOption.value = option
    }

    fun setDESC(){
        _desc.value = !_desc.value
    }

    fun loadSortingOptions() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun loadData() {
        _isLoading.value = true
        var currentPage = 0
        val allNotifications = mutableListOf<NotificationResponse>()

        try{
            while(true){
                val response = BaseRepository(userPreferencesRepository)
                    .notificationRepository.getAllNotifications(filter = "",page = currentPage)
                if(response.isSuccessful){
                    val page = response.body()
                    if(page != null){
                        allNotifications.addAll(page.content)
                        if(page.page >= (page.totalPages - 1)){
                            break
                        }
                        currentPage++
                        _notiList.value = allNotifications.toMutableList()
                    }
                    else break
                } else{
                    break
                    }
                }
                _notiList.value = allNotifications.toMutableList()
            } catch (e: Exception){

        } finally {
            _isLoading.value = false
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ListNotificationViewModel(application.userPreferencesRepository)
            }
        }
    }
}