package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.user.UserResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class ListUserViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    private val _user = MutableStateFlow<MutableList<UserResponse>>(mutableListOf())
    val user: StateFlow<List<UserResponse>> = _user

    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf(
        "Tất cả",
        "Tên người dùng", //Fullname
        "Tên hiển thị" //Username
    ))
    val sortingOptions: StateFlow<List<String>> = _sortingOptions

    private val _selectedSortingOption = MutableStateFlow<String>(_sortingOptions.value[0])
    val selectedSortingOption: StateFlow<String> = _selectedSortingOption

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchHistory = MutableStateFlow<List<String>>(mutableListOf())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory

    private val _asc = MutableStateFlow(true)
    val asc = _asc.asStateFlow()

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

    suspend fun searchUser() {
        _isLoading.value = true
        var currentPage = 0
        val allUsers = mutableListOf<UserResponse>()

        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .userRepository.getAllUsers(filter = "username ~~ '*${_searchQuery.value}*' or fullname ~~ '*${_searchQuery.value}*' or email ~~ '*${_searchQuery.value}*' or phone ~~ '*${_searchQuery.value}*'", page = currentPage)
                if (response.isSuccessful) {
                    setSearchHistory(_searchQuery.value)
                    val page = response.body()
                    if (page != null){
                        allUsers.addAll(page.content)
                        _user.value = allUsers.filter { it.role.name != "ADMIN" }.toMutableList()
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }
            _user.value = allUsers.filter { it.role.name != "ADMIN" }.toMutableList() // Update UI with all categories
        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun sortUser(){
        _isLoading.value = true
        var currentPage = 0
        val allUsers = mutableListOf<UserResponse>()
        val sortOption = when(_selectedSortingOption.value){
            "Tên hiển thị" -> "username"
            "Tên người dùng" -> "fullname"
            else -> "id"
        }
        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .userRepository.getAllUsers(
                        filter = "",
                        page = currentPage,
                        sort = listOf("${sortOption}," + if (_asc.value) "asc" else "desc"))

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allUsers.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _user.value = allUsers.filter { it.role.name != "ADMIN" }.toMutableList()
                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }
            _user.value = allUsers.filter { it.role.name != "ADMIN" }.toMutableList() // Update UI with all categories
        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }

    fun setSelectedSortingOption(option: String) {
        _selectedSortingOption.value = option
    }

    fun setASC(){
        _asc.value = !_asc.value
    }

    suspend fun loadData() {
        _isLoading.value = true
        var currentPage = 0
        val allUsers = mutableListOf<UserResponse>()

        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .userRepository.getAllUsers(filter = "", page = currentPage)
                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allUsers.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _user.value = allUsers.filter { it.role.name != "ADMIN" }.toMutableList()
                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }
            _user.value = allUsers.filter { it.role.name != "ADMIN" }.toMutableList() // Update UI with all categories
        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun deleteUser(id: Long): Int{
        _isDeleting.value = true
        try {
            Log.d("User", "Bắt đầu xóa Nguoi dung")
            Log.d(
                "User",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response =
                BaseRepository(userPreferencesRepository).productCategoryRepository.deleteCategory(id)
            Log.d("User", "User code: ${response.code()}")
            if (response.isSuccessful) {
                _user.value = _user.value.filterNot { it.id == id }.toMutableList()
                return 1
            } else {
                Log.d("User", "Xóa thất bại: ${response.errorBody()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("User", "Không thể xóa: ${e.message}")
            return -1
        } finally {
            _isDeleting.value = false
            Log.d("User", "Kết thúc xóa")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ListUserViewModel(application.userPreferencesRepository)
            }
        }
    }
}