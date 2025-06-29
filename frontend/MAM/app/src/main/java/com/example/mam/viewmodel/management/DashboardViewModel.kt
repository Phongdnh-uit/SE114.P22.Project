package com.example.mam.viewmodel.management

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import co.yml.charts.common.model.Point
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _processingOrderNum = MutableStateFlow(0)
    val processingOrderNum: StateFlow<Int> = _processingOrderNum

    private val _notProcessingOrderNum = MutableStateFlow(0)
    val notProcessingOrderNum: StateFlow<Int> = _notProcessingOrderNum

    private val _revenueList = MutableStateFlow(listOf<Point>())
    val revenueList = _revenueList.asStateFlow()


    private val _isLoadingRevenue = MutableStateFlow(false)
    val isLoadingRevenue: StateFlow<Boolean> = _isLoadingRevenue

    private val _categorySoldList = MutableStateFlow(listOf<Pair<String,Float>>())
    val categorySoldList = _categorySoldList.asStateFlow()

    private val _categorySoldQuarterList = MutableStateFlow(listOf<Pair<String,Float>>())
    val categorySoldQuarterList = _categorySoldQuarterList.asStateFlow()

    private val _isLoadingCategory = MutableStateFlow(false)
    val isLoadingCategory: StateFlow<Boolean> = _isLoadingCategory

    fun loadData() {
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

    suspend fun loadRevenueMonthlyList(year: Int, isMonthly: Boolean) {
        try {
            val groupBy = if(isMonthly) "month" else "quarter"
            _isLoadingRevenue.value = true
            val response = BaseRepository(userPreferencesRepository).stastiticRepository.getRevenueByMonthOrQuarter(
                year,
                groupBy
            )
            if (response.isSuccessful){
                _revenueList.value = response.body()?.map {
                    Point(it.key.toFloat(), it.value.toFloat())
                } ?: emptyList()
            }

        } catch (e: Exception) {
            // Handle error
        } finally {
            _isLoadingRevenue.value = false
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun loadCategorySoldMonthlyList(month: Int, year: Int) {
        try {
            _isLoadingCategory.value = true
            val response = BaseRepository(userPreferencesRepository).stastiticRepository.getSoldByCategory(month,year)
            if (response.isSuccessful)
            _categorySoldList.value = response.body()?.map {
                Pair(it.key, it.value.toFloat())
            } ?: emptyList()
        } catch (e: Exception) {
            // Handle error
        } finally {
            _isLoadingCategory.value = false
        }
    }

    suspend fun countOrderByStatus(): Map<String, Long>{
        try {
            val response = BaseRepository(userPreferencesRepository).stastiticRepository.getStatusCount()
            return if (response.isSuccessful)
                response.body()!!
            else emptyMap()
        }
        catch (e: Exception){
            return emptyMap()
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                DashboardViewModel(application.userPreferencesRepository)
            }
        }
    }
}