package com.example.mam.viewmodel.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.promotion.PromotionResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListPromotionViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _promoList = MutableStateFlow<MutableList<PromotionResponse>>(mutableListOf())
    val promoList: StateFlow<List<PromotionResponse>> = _promoList

    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf(
        "Tất cả",
        "Mã khuyến mãi",
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

    suspend fun searchPromotion() {
        //search and save history
        _isLoading.value = true
        var currentPage = 0
        val allPromotions = mutableListOf<PromotionResponse>()

        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .promotionRepository.getAllPromotions(filter = "code ~~ '*${_searchQuery.value}*' or description ~~ '*${_searchQuery.value}*'", page = currentPage)

                if (response.isSuccessful) {
                    setSearchHistory(_searchQuery.value)
                    val page = response.body()
                    if (page != null){
                        allPromotions.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _promoList.value = allPromotions.toMutableList()

                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }

            _promoList.value = allPromotions.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun sortPromotion(){
        _isLoading.value = true
        var currentPage = 0
        val allPromotions = mutableListOf<PromotionResponse>()
        val sortOption = when(_selectedSortingOption.value){
            "Mã khuyến mãi" -> "code"
            else -> "id"
        }
        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .promotionRepository.getAllPromotions(
                        filter = "",
                        page = currentPage,
                        sort = listOf("${sortOption}," + if (_asc.value) "asc" else "desc"))

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allPromotions.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _promoList.value = allPromotions.toMutableList()

                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }
            _promoList.value = allPromotions.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }
    fun setASC(){
        _asc.value = !_asc.value
    }
    fun setSelectedSortingOption(option: String) {
        _selectedSortingOption.value = option
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

    suspend fun deletePromo(id: Long): Int{
        try {
            val response = BaseRepository(userPreferencesRepository).promotionRepository.deletePromotion(id)
            return if (response.isSuccessful){
                _promoList.value = _promoList.value.filter {it.id != id}.toMutableList()
                1
            } else 0
        }
        catch (e: Exception) {
            return 0
        }
    }

    suspend fun loadData() {
        _isLoading.value = true
        var currentPage = 0
        val allPromotions = mutableListOf<PromotionResponse>()

        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .promotionRepository.getAllPromotions(filter = "", page = currentPage)

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allPromotions.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _promoList.value = allPromotions.toMutableList()

                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }
            _asc.value = true // Reset sorting order to ascending
            _promoList.value = allPromotions.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ListPromotionViewModel(application.userPreferencesRepository)
            }
        }
    }
}
