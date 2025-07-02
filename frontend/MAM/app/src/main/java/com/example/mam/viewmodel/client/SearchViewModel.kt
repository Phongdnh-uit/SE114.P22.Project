package com.example.mam.viewmodel.client

import android.util.Log
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
import com.example.mam.dto.product.ProductResponse
import com.example.mam.repository.paging.createPagingFlow
import com.example.mam.repository.retrofit.BaseRepository
import com.google.android.gms.analytics.ecommerce.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest

class SearchViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf(
        "Tất cả",
        "Tên",
        "Giá",
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
            "Tên" -> "name"
            "Giá" -> "originalPrice"
            else -> "id"
        }
        sort.value = if (_asc.value) listOf("$sortOption,asc") else listOf("$sortOption,desc")
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val products: Flow<PagingData<ProductResponse>> = combine(
        search,
        sort
    ) { filter, sort ->
        Pair(filter, sort)
    }.flatMapLatest { (filter, sort) ->
        createPagingFlow(
            filter = filter,
            sort = sort
        ) { page, size, s, f ->
            BaseRepository(userPreferencesRepository).productRepository.getAllProducts(
                filter = "name ~~ '*${f}*' " +
                        "or shortDescription ~~ '*${f}*'" +
                        "or detailDescription ~~ '*${f}*'",
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


//    suspend fun searchProduct() {
//        //search and save history
//        _isLoading.value = true
//        var currentPage = 0
//        val allProducts = mutableListOf<ProductResponse>()
//
//        try {
//            Log.d("SearchViewModel", "Bắt đầu tìm kiếm sản phẩm")
//            Log.d("SearchViewModel", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")
//
//            while (true) { // Loop until the last page
//                val response = BaseRepository(userPreferencesRepository)
//                    .productRepository.getAllProducts(filter = "name ~~ '*${_searchQuery.value}*' " +
//                            "or shortDescription ~~ '*${_searchQuery.value}*'" +
//                            "or detailDescription ~~ '*${_searchQuery.value}*'"
//                        , page = currentPage, size = 20)
//
//                Log.d("SearchViewModel", "Status code: ${response.code()}")
//
//                if (response.isSuccessful) {
//                    setSearchHistory(_searchQuery.value)
//                    val page = response.body()
//                    if (page != null){
//                        allProducts.addAll(page.content)
//                        if (page.page >= (page.totalPages - 1)) {
//                            break // Stop looping when the last page is reached
//                        }
//                        currentPage++ // Move to the next page
//                        Log.d("SearchViewModel", "Lấy trang ${page.page}")
//                        _products.value = allProducts.toMutableList()
//
//                    }
//                    else break
//                } else {
//                    Log.d("SearchViewModel", "Tim San pham thất bại: ${response.errorBody()?.string()}")
//                    break // Stop loop on failure
//                }
//            }
//
//            _products.value = allProducts.toMutableList() // Update UI with all categories
//
//        } catch (e: Exception) {
//            Log.d("SearchViewModel", "Không thể tìm kiếm sản phẩm: ${e.message}")
//        } finally {
//            _isLoading.value = false
//        }
//    }
//
//    suspend fun sortProduct(){
//        _isLoading.value = true
//        var currentPage = 0
//        val allProducts = mutableListOf<ProductResponse>()
//        val sortOption = when(_selectedSortingOption.value){
//            "Tên" -> "name"
//            "Giá" -> "originalPrice"
//            else -> "id"
//        }
//        try {
//            Log.d("SearchViewModel", "Bắt đầu sort sản phẩm")
//            Log.d("SearchViewModel", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")
//
//            while (true) { // Loop until the last page
//                val response = BaseRepository(userPreferencesRepository)
//                    .productRepository.getAllProducts(
//                        filter = "",
//                        sort = if (_asc.value) listOf("$sortOption,asc") else listOf("$sortOption,desc"),
//                        page = currentPage, size = 20)
//
//                Log.d("SearchViewModel", "Status code: ${response.code()}")
//
//                if (response.isSuccessful) {
//                    setSearchHistory(_searchQuery.value)
//                    val page = response.body()
//                    if (page != null){
//                        allProducts.addAll(page.content)
//                        if (page.page >= (page.totalPages - 1)) {
//                            break // Stop looping when the last page is reached
//                        }
//                        currentPage++ // Move to the next page
//                        Log.d("SearchViewModel", "Lấy trang ${page.page}")
//                        _products.value = allProducts.toMutableList()
//
//                    }
//                    else break
//                } else {
//                    Log.d("SearchViewModel", "Sort San pham thất bại: ${response.errorBody()?.string()}")
//                    break // Stop loop on failure
//                }
//            }
//
//            _products.value = allProducts.toMutableList() // Update UI with all categories
//
//        } catch (e: Exception) {
//            Log.d("SearchViewModel", "Không thể sort sản phẩm: ${e.message}")
//        } finally {
//            _isLoading.value = false
//        }
//    }
//    suspend fun loadData() {
//        _isLoading.value = true
//        var currentPage = 0
//        val allProducts = mutableListOf<ProductResponse>()
//
//        try {
//            Log.d("SearchViewModel", "Bắt đầu lay sản phẩm")
//            Log.d("SearchViewModel", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")
//
//            while (true) { // Loop until the last page
//                val response = BaseRepository(userPreferencesRepository)
//                    .productRepository.getAllProducts(filter = "", page = currentPage, size = 20)
//
//                Log.d("SearchViewModel", "Status code: ${response.code()}")
//
//                if (response.isSuccessful) {
//                    setSearchHistory(_searchQuery.value)
//                    val page = response.body()
//                    if (page != null){
//                        allProducts.addAll(page.content)
//                        if (page.page >= (page.totalPages - 1)) {
//                            break // Stop looping when the last page is reached
//                        }
//                        currentPage++ // Move to the next page
//                        Log.d("SearchViewModel", "Lấy trang ${page.page}")
//                        _products.value = allProducts.toMutableList()
//
//                    }
//                    else break
//                } else {
//                    Log.d("SearchViewModel", "Lay San pham thất bại: ${response.errorBody()?.string()}")
//                    break // Stop loop on failure
//                }
//            }
//
//            _products.value = allProducts.toMutableList() // Update UI with all categories
//
//        } catch (e: Exception) {
//            Log.d("SearchViewModel", "Không thể lay sản phẩm: ${e.message}")
//        } finally {
//            _isLoading.value = false
//        }
//    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                SearchViewModel(application.userPreferencesRepository)
            }
        }
    }
}