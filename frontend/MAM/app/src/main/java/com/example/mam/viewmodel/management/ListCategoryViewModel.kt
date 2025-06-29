package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.product.CategoryResponse
import com.example.mam.repository.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class ListCategoryViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    private val _categories = MutableStateFlow<MutableList<CategoryResponse>>(mutableListOf())
    val categories: StateFlow<List<CategoryResponse>> = _categories

    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf(
        "Tất cả",
        "Tên",
    ))
    val sortingOptions: StateFlow<List<String>> = _sortingOptions

    private val _selectedSortingOption = MutableStateFlow<String>(_sortingOptions.value[0])
    val selectedSortingOption: StateFlow<String> = _selectedSortingOption

    private val _asc = MutableStateFlow(true)
    val asc = _asc.asStateFlow()

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchHistory = MutableStateFlow<List<String>>(mutableListOf())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory

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

    suspend fun searchCategory() {
        //search and save history

        var currentPage = 0
        val allCategories = mutableListOf<CategoryResponse>()

        try {
            Log.d("Category", "Bắt đầu tim kiem Danh mục")
            Log.d("Category", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")

            while (true) { // Loop until the last page
                _isLoading.value = true
                val response = BaseRepository(userPreferencesRepository)
                    .productCategoryRepository.getCategories(filter = "name ~~ '*${_searchQuery.value}*' or description ~~ '*${_searchQuery.value}*'", page = currentPage)

                Log.d("Category", "Status code: ${response.code()}")

                if (response.isSuccessful) {
                    setSearchHistory(_searchQuery.value)
                    val page = response.body()
                    if (page != null){
                        allCategories.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        Log.d("Category", "Lấy trang ${page.page}")
                        _categories.value = allCategories.toMutableList()
                        _isLoading.value = false // Stop loading after each page fetch
                    }
                    else break
                } else {
                    Log.d("Category", "Tim Danh mục thất bại: ${response.errorBody()}")
                    break // Stop loop on failure
                }
            }

            _categories.value = allCategories.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.d("Category", "Không thể tim kiem Danh mục: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Category", "Kết thúc tim kiem Danh mục")
        }
    }

    suspend fun sortCategory(){
        _isLoading.value = true
        var currentPage = 0
        val allCategories = mutableListOf<CategoryResponse>()
        val sortOption = when(_selectedSortingOption.value){
            "Tên" -> "name"
            else -> "id"
        }
        try {
            Log.d("Category", "Bắt đầu sort Danh mục")
            Log.d("Category", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")

            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .productCategoryRepository.getCategories(
                        filter = "",
                        page = currentPage,
                        sort = listOf("${sortOption}," + if (_asc.value) "asc" else "desc"))

                Log.d("Category", "Status code: ${response.code()}")

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allCategories.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        Log.d("Category", "Lấy trang ${page.page}")
                        _categories.value = allCategories.toMutableList()

                    }
                    else break
                } else {
                    Log.d("Category", "Sort Danh mục thất bại: ${response.errorBody()}")
                    break // Stop loop on failure
                }
            }

            _categories.value = allCategories.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.d("Category", "Không thể tim kiem Danh mục: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Category", "Kết thúc tim kiem Danh mục")
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
        val allCategories = mutableListOf<CategoryResponse>()

        try {
            Log.d("Category", "Bắt đầu lấy Danh mục")
            Log.d("Category", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")

            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .productCategoryRepository.getCategories(filter = "", page = currentPage)

                Log.d("Category", "Status code: ${response.code()}")

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allCategories.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        Log.d("Category", "Lấy trang ${page.page}")
                        _categories.value = allCategories.toMutableList()

                    }
                    else break
                } else {
                    Log.d("Category", "Lấy Danh mục thất bại: ${response.errorBody()?.string()}")
                    break // Stop loop on failure
                }
            }

            _categories.value = allCategories.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.d("Category", "Không thể lấy Danh mục: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Category", "Kết thúc lấy Danh mục")
        }
    }

    suspend fun deleteCategory(id: Long): Int{

        _isDeleting.value = true
        try {
            Log.d("Category", "Bắt đầu xóa Danh mục")
            Log.d(
                "Category",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response =
                BaseRepository(userPreferencesRepository).productCategoryRepository.deleteCategory(id)
            Log.d("Category", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                _categories.value = _categories.value.filterNot { it.id == id }.toMutableList()
                return 1
            } else {
                Log.d("Category", "Xóa Danh mục thất bại: ${response.errorBody()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Category", "Không thể xóa Danh mục: ${e.message}")
            return -1
        } finally {
            _isDeleting.value = false
            Log.d("Category", "Kết thúc xóa Danh mục")
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ListCategoryViewModel(application.userPreferencesRepository)
            }
        }
    }
}