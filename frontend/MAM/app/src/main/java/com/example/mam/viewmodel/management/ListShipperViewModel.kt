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
import com.example.mam.dto.shipper.ShipperResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListShipperViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    private val _shippers = MutableStateFlow<MutableList<ShipperResponse>>(mutableListOf())
    val shippers: StateFlow<List<ShipperResponse>> = _shippers

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

    suspend fun searchShipper() {
        _isLoading.value = true
        var currentPage = 0
        val allShippers = mutableListOf<ShipperResponse>()

        try {
            Log.d("Shipper", "Bắt đầu tim kiem Shipper")
            Log.d("Shipper", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")

            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .shipperRepository.getShippers(
                        filter = "fullname ~~ '*${_searchQuery.value}*' or phone ~~ '*${_searchQuery.value}*' or licensePlate ~~ '*${_searchQuery.value}*'",
                        page = currentPage
                    )

                Log.d("Shipper", "Status code: ${response.code()}")

                if (response.isSuccessful) {
                    setSearchHistory(_searchQuery.value)
                    val page = response.body()
                    if (page != null){
                        allShippers.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        Log.d("Shipper", "Lấy trang ${page.page}")
                        _shippers.value = allShippers.toMutableList()
                    }
                    else break
                } else {
                    Log.d("Shipper", "Tim Shipper thất bại: ${response.errorBody()}")
                    break // Stop loop on failure
                }
            }
            _shippers.value = allShippers.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.d("Shipper", "Không thể lấy Shipper: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Shipper", "Kết thúc lấy Shipper")
        }
    }

    suspend fun sortShipper(){
        _isLoading.value = true
        var currentPage = 0
        val allShippers = mutableListOf<ShipperResponse>()
        val sortOption = when(_selectedSortingOption.value){
            "Tên" -> "fullname"
            else -> "id"
        }
        try{
            Log.d("Shipper", "Bắt đầu lấy Shipper")
            Log.d("Shipper", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")

            while(true){
                val response = BaseRepository(userPreferencesRepository)
                    .shipperRepository.getShippers(
                        filter = "",
                        page = currentPage,
                        sort = listOf("${sortOption}," + if (_asc.value) "asc" else "desc"))
                Log.d("Shipper", "Status code: ${response.code()}")

                if(response.isSuccessful){
                    val page = response.body()
                    if (page != null){
                        allShippers.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++
                        Log.d("Shipper","Lấy trang ${page.page}")
                        _shippers.value = allShippers.toMutableList()
                    }
                    else break
                } else{
                    Log.d("Shipper", "Lấy Shipper thất bại: ${response.errorBody()}")
                    break // Stop loop on failure
                }
            }
            _shippers.value = allShippers.toMutableList()
        } catch (e: Exception) {
            Log.d("Shipper", "Không thể lấy Shipper: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Shipper", "Kết thúc lấy Shipper")
        }
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

    suspend fun loadData() {
        _isLoading.value = true
        var currentPage = 0
        val allShippers = mutableListOf<ShipperResponse>()

        try {
            Log.d("Shipper", "Bắt đầu lấy Shipper")
            Log.d("Shipper", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")

            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .shipperRepository.getShippers(filter = "", page = currentPage)

                Log.d("Shipper", "Status code: ${response.code()}")

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allShippers.addAll(page.content)
                        Log.d("Shipper", "Số lượng Shipper: ${allShippers.size}")
                        _shippers.value = allShippers.toMutableList()
                        Log.d("Shipper", "Số lượng Shipper hiện tại: ${_shippers.value.size}")
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        Log.d("Shipper", "Lấy trang ${page.page}")

                    }
                    else break
                } else {
                    Log.d("Shipper", "Lấy Shipper thất bại: ${response.errorBody()}")
                    break // Stop loop on failure
                }
            }
            _shippers.value = allShippers.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.d("Shipper", "Không thể lấy Shipper: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Shipper", "Kết thúc lấy Shipper")
        }
    }

    fun setASC(){
        _asc.value = !_asc.value
    }

    suspend fun deleteShipper(id: Long): Int{
        _isDeleting.value = true
        try{
            Log.d("Shipper", "Bắt đầu xóa Shipper")
            Log.d("Shipper", "DSAccessToken: ${userPreferencesRepository.accessToken.first()}")
            val response = BaseRepository(userPreferencesRepository).shipperRepository.deleteShipper(id)
            Log.d("Shipper", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                _shippers.value = _shippers.value.filterNot { it.id == id }.toMutableList()
                return 1
            } else {
                Log.d("Shipper", "Xóa Shipper thất bại: ${response.errorBody()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Shipper", "Không thể xóa Shipper: ${e.message}")
            return -1
        } finally {
            _isDeleting.value = false
            Log.d("Shipper", "Ket thuc xoa Shipper")
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ListShipperViewModel(application.userPreferencesRepository)
            }
        }
    }
}