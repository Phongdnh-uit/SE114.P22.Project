package com.example.mam.viewmodel.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.notification.NotificationResponse
import com.example.mam.dto.promotion.PromotionResponse
import com.example.mam.repository.paging.createPagingFlow
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ListPromotionViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf(
        "Tất cả",
        "Ngày bắt đầu",
        "Ngày kết thúc",
        "Mã giảm giá",
        "Giá trị giảm giá",
        "Giá trị áp dụng" // Thêm tùy chọn sắp xếp mới
    ))
    val sortingOptions: StateFlow<List<String>> = _sortingOptions

    private val _selectedSortingOption = MutableStateFlow<String>(_sortingOptions.value[0])
    val selectedSortingOption = _selectedSortingOption.asStateFlow()

    private val _asc = MutableStateFlow(true)
    val asc = _asc.asStateFlow()

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchHistory = MutableStateFlow<List<String>>(mutableListOf())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory

    private val search = MutableStateFlow<String>("")
    private val sort = MutableStateFlow<List<String>>(listOf("id,desc"))
    fun search(){
        search.value = _searchQuery.value
        setSearchHistory(_searchQuery.value)
    }

    fun sort(){
        val sortOption = when(_selectedSortingOption.value){
            "Ngày bắt đầu" -> "startDate"
            "Ngày kết thúc" -> "endDate"
            "Mã giảm giá" -> "code"
            "Giá trị giảm giá" -> "discountValue"
            "Giá trị áp dụng" -> "minValue"
            else -> "id"
        }
        sort.value = if (_asc.value) listOf("$sortOption,asc") else listOf("$sortOption,desc")
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val promotions: Flow<PagingData<PromotionResponse>> = combine(
        search,
        sort
    ) { filter, sort ->
        Pair(filter, sort)
    }.flatMapLatest { (filter, sort) ->
        createPagingFlow(
            filter = filter,
            sort = sort
        ) { page, size, s, f ->
            BaseRepository(userPreferencesRepository).promotionRepository.getAllPromotions(
                filter = "discountValue ~~ '*${f}*' " +
                        "or minValue ~~ '*${f}*' " +
                        "or code ~~ '*${f}*' " +
                        "or startDate ~~ '*${f}*' " +
                        "or endDate ~~ '*${f}*' " +
                        "or description ~~ '*${f}*'" +
                        "or created_at ~~ '*${f}*'"+
                        "or updated_at ~~ '*${f}*'"+
                        "or id ~~ '*${f}*'",
                sort = s,
                page = page,
                size = size
            ).body()?.content ?: emptyList()
        }
    }.cachedIn(viewModelScope)

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

    fun setSelectedSortingOption(option: String) {
        _selectedSortingOption.value = option
    }
    fun setASC(){
        _asc.value = !_asc.value
    }

    suspend fun deletePromo(id: Long): Int{
        try {
            val response = BaseRepository(userPreferencesRepository).promotionRepository.deletePromotion(id)
            return if (response.isSuccessful){
                1
            } else 0
        }
        catch (e: Exception) {
            return 0
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
