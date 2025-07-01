package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.product.ProductResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListProductViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    private val _product = MutableStateFlow<MutableList<ProductResponse>>(mutableListOf())
    val product: StateFlow<List<ProductResponse>> = _product

    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf(
        "Tất cả",
        "Tên",
        "Giá"
    ))
    val sortingOptions: StateFlow<List<String>> = _sortingOptions

    private val _asc = MutableStateFlow(true)
    val asc = _asc.asStateFlow()

    private val _selectedSortingOption = MutableStateFlow<String>(_sortingOptions.value[0])
    val selectedSortingOption: StateFlow<String> = _selectedSortingOption

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

    suspend fun searchProduct() {
        _isLoading.value = true
        var currentPage = 0
        val allProducts = mutableListOf<ProductResponse>()

        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .productRepository.getAllProducts(filter = "name ~~ '*${_searchQuery.value}*' or shortDescription ~~ '*${_searchQuery.value}*' or detailDescription ~~ '*${_searchQuery.value}*'", page = currentPage)

                if (response.isSuccessful) {
                    setSearchHistory(_searchQuery.value)
                    val page = response.body()
                    if (page != null){
                        allProducts.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _product.value = allProducts.toMutableList()
                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }
            _product.value = allProducts.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.e("ListProductViewModel", "Error searching products: ${e.message}")
            // Handle error, maybe show a message to the user
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun sortProduct(){
        _isLoading.value = true
        var currentPage = 0
        val allProducts = mutableListOf<ProductResponse>()
        val sortOption = when(_selectedSortingOption.value){
            "Tên" -> "name"
            else -> "id"
        }
        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .productRepository.getAllProducts(
                        filter = "",
                        page = currentPage,
                        sort = listOf("${sortOption}," + if (_asc.value) "asc" else "desc"))

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allProducts.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _product.value = allProducts.toMutableList()
                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }

            _product.value = allProducts.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.e("ListProductViewModel", "Error sorting products: ${e.message}")
            // Handle error, maybe show a message to the user
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
        val allProducts = mutableListOf<ProductResponse>()

        try {
            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .productRepository.getAllProducts(filter = "", page = currentPage)
                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allProducts.addAll(page.content)
                        _product.value = allProducts.toMutableList()
                        _asc.value = true // Reset sorting order to ascending
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

            _product.value = allProducts.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.e("ListProductViewModel", "Error loading products: ${e.message}")
            // Handle error, maybe show a message to the user
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun deleteProduct(id: Long): Int{

        _isDeleting.value = true
        try {
            val response =
                BaseRepository(userPreferencesRepository).productRepository.deleteProduct(id)
            if (response.isSuccessful) {
                _product.value = _product.value.filterNot { it.id == id }.toMutableList()
                Log.d("ListProductViewModel", "Product with id $id deleted successfully")
                return 1
            } else {
                Log.e("ListProductViewModel", "Failed to delete product with id $id: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.e("ListProductViewModel", "Error deleting product: ${e.message}")
            return -1
        } finally {
            _isDeleting.value = false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ListProductViewModel(application.userPreferencesRepository)
            }
        }
    }
}