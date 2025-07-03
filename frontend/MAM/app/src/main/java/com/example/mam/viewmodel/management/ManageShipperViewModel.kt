package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.shipper.ShipperRequest
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class ManageShipperViewModel(
    savedStateHandle: SavedStateHandle?,
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {
    private val shipperId = savedStateHandle?.get<Long>("shipperId") ?:0L

    private val _shipperID = MutableStateFlow<Long>(shipperId)
    val shipperID: StateFlow<Long> = _shipperID

    private val _shipperName = MutableStateFlow<String>("")
    val shipperName: MutableStateFlow<String> get() = _shipperName

    private val _shipperPhone = MutableStateFlow<String>("")
    val shipperPhone: MutableStateFlow<String> get() = _shipperPhone

    private val _shipperLicense = MutableStateFlow<String>("")
    val shipperLicense: MutableStateFlow<String> get() = _shipperLicense

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun setShipperName(name: String) {
        _shipperName.value = name
    }

    fun isShipperNameValid(): String {
        val name = _shipperName.value.trim()
        val regex = "^[\\p{L} ]+$".toRegex()
        if (name.length > 50) {
            return "Tên quá dài (>50 ký tự)" // Name is too long
        }
        else if (name.length < 5) {
            return "Tên quá ngắn (<5 ký tự)" // Name is too short
        }
        else if (!regex.matches(name)) {
            return "Tên không hợp lệ" // Name contains invalid characters
        }
        return "" // Name is valid
    }

    fun setShipperPhone(phone: String) {
        _shipperPhone.value = phone
    }

    fun isPhoneValid(): String {
        val phone = _shipperPhone.value.trim()
        val regex = "^0\\d{9}$".toRegex()
        if (!regex.matches(phone)) {
            return "Số điện thoại không hợp lệ" // Phone contains invalid characters
        }
        return "" // Phone is valid
    }

    fun setShipperLicense(license: String) {
        _shipperLicense.value = license
    }

    fun isLicenseValid(): String {
        val license = _shipperLicense.value.trim()
        val regex = "^([1-9][0-9])-[ABCDEFGHKLM][1-9] \\d{3}\\.\\d{2}\$".toRegex()
        if (!regex.matches(license)) {
            return "Biển số không hợp lệ" // License contains invalid characters
        }
        return "" // License is valid
    }

    suspend fun loadData() {
        _isLoading.value = true
        try {
            Log.d("Shipper", "Bắt đầu lấy Shipper")
            Log.d(
                "Shipper",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response =
                BaseRepository(userPreferencesRepository)
                    .shipperRepository
                    .getShipperById(_shipperID.value)
            Log.d("Shipper", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val shipper = response.body()
                if (shipper != null) {
                    _shipperName.value = shipper.fullname
                    _shipperPhone.value = shipper.phone
                    _shipperLicense.value = shipper.licensePlate
                    Log.d("Shipper", "Lấy Shipper thành công")
                }

            } else {
                Log.d("Shipper", "Lấy Shipper thất bại: ${response.errorBody().toString()}")
            }
        } catch (e: Exception) {
            Log.d("Shipper", "Không thể lấy Shipper: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Shipper", "Kết thúc lấy Shipper")
        }

    }

    fun mockData() {
        _shipperID.value = 123456
        _shipperName.value = "Nguyễn Văn A"
        _shipperPhone.value = "0123456789"
        _shipperLicense.value = "55-A1 123.45"
    }

    fun clearData() {
        _shipperID.value = 0
        _shipperName.value = ""
        _shipperPhone.value = ""
        _shipperLicense.value = ""
    }

    suspend fun updateShipper(): Int {
        _isLoading.value = true
        try {
            Log.d("Shipper", "Bắt đầu cap nhat Shipper")
            Log.d(
                "Shipper",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val request = ShipperRequest(
                _shipperName.value,
                _shipperPhone.value,
                _shipperLicense.value
            )
            val response = BaseRepository(userPreferencesRepository)
                .shipperRepository
                .updateShipper(_shipperID.value, request)
            Log.d("Shipper", "${_shipperName.value}, ${_shipperLicense.value}, ${_shipperPhone.value}")
            if (response == null){
                return 0
            }
            Log.d("Shipper", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val shipper = response.body()
                if (shipper != null) {
                    _shipperName.value = shipper.fullname
                    _shipperPhone.value = shipper.phone
                    _shipperLicense.value = shipper.licensePlate
                    Log.d("Shipper", "Cap nhat Shipper thành công")
                }
                return 1
            } else {
                Log.d("Shipper", "Cap nhat Shipper thất bại: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Shipper", "Không thể cap nhat Shipper: ${e.message}")
            return 0
        } finally {
            _isLoading.value = false
            Log.d("Shipper", "Kết thúc cap nhat Shipper")
        }
    }

    suspend fun addShipper(): Int {
        _isLoading.value = true
        try {
            Log.d("Shipper", "Bắt đầu them ")
            Log.d(
                "Shipper",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val request = ShipperRequest(
                _shipperName.value,
                _shipperPhone.value,
                _shipperLicense.value
            )
            val response = BaseRepository(userPreferencesRepository)
                .shipperRepository
                .createShipper(request)

            if (response == null){
                return 0
            }
            Log.d("Shipper", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val shipper = response.body()
                if (shipper != null) {
                    _shipperName.value = shipper.fullname
                    _shipperPhone.value = shipper.phone
                    _shipperLicense.value = shipper.licensePlate
                    Log.d("Shipper", "Them Shipper thành công")
                }
                return 1
            } else {
                Log.d("Shipper", "Them Shipper thất bại: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Shipper", "Không thể them Shipper: ${e.message}")
            return 0
        } finally {
            _isLoading.value = false
            Log.d("Shipper", "Kết thúc them Shipper")
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                ManageShipperViewModel(
                    savedStateHandle = savedStateHandle,
                    userPreferencesRepository = application.userPreferencesRepository,
                )
            }
        }
    }
}