package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.Constant
import com.example.mam.data.Constant.BASE_IMAGE
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.repository.BaseRepository
import com.example.mam.viewmodel.ImageViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.Instant

class ManageUserViewModel(
    savedStateHandle: SavedStateHandle?,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val imageViewModel: ImageViewModel
): ViewModel() {
    private val userId = savedStateHandle?.get<Long>("userId") ?: 0L

    private val _userID = MutableStateFlow<Long>(userId)
    val userID: StateFlow<Long> = _userID

    private val _userName = MutableStateFlow<String>("")
    val userName = _userName.asStateFlow()

    private val _userImage = MutableStateFlow<String>(BASE_IMAGE)
    val userImage: StateFlow<String> = _userImage

    private val _userImageFile = MutableStateFlow<File?>(null) // Lưu ảnh dạng Multipart

    private val _fullName = MutableStateFlow<String>("")
    val fullName = _fullName.asStateFlow()

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _role = MutableStateFlow<String>("")
    val role = _role.asStateFlow()

    private val _phone = MutableStateFlow<String>("")
    val phone = _phone.asStateFlow()

    private val _statusList = MutableStateFlow<List<String>>(listOf())
    val statusList = _statusList.asStateFlow()

    private val _status = MutableStateFlow<String>("")
    val status = _status.asStateFlow()

    private val _createAt = MutableStateFlow<Instant>(Instant.now())
    val createAt = _createAt.asStateFlow()

    private val _updateAt = MutableStateFlow<Instant>(Instant.now())
    val updateAt = _updateAt.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun setUserName(userName: String) {
        _userName.value = userName
    }

    fun setUserImage(imgUrl: String) {
        _userImage.value = imgUrl
    }

    fun setFullName(fullName: String) {
        _fullName.value = fullName
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPhone(phone: String) {
        _phone.value = phone
    }

    fun setStatus(status: String) {
        _status.value = status
    }

    suspend fun loadData(){
        _isLoading.value = true
        try {
            Log.d("User", "Bắt đầu lấy Nguoi dung")
            Log.d(
                "User",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val statusList = BaseRepository(userPreferencesRepository)
                .authPublicRepository.getMetadata(listOf(Constant.metadata.USER_STATUS.name))
            if (statusList.isSuccessful) {
                _statusList.value = statusList.body()?.get(Constant.metadata.USER_STATUS.name) ?: listOf()
            } else {
                Log.d("User", "Lấy danh sách trạng thái thất bại: ${statusList.errorBody()?.string()}")
            }
            val response = BaseRepository(userPreferencesRepository)
                    .userRepository
                    .getUserById(_userID.value)
            Log.d("User", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    _userID.value = user.id
                    _userName.value = user.username
                    _fullName.value = user.fullname
                    _email.value = user.email
                    _role.value = user.role.name
                    _phone.value = user.phone
                    _status.value = user.status
                    _createAt.value = Instant.parse(user.createdAt)
                    _updateAt.value = Instant.parse(user.updatedAt)
                    _userImage.value = user.getRealURL()
                    Log.d("User", _userImage.value)
                }

            } else {
                Log.d("User", "Lấy Nguoi dung thất bại: ${response.errorBody().toString()}")
            }
        } catch (e: Exception) {
            Log.d("User", "Không thể lấy Nguoi dung: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("user", "Kết thúc lấy Nguoi dung")
        }
    }

    suspend fun updateUserStatus(): Int {
        _isLoading.value = true
        try {
            Log.d("User", "Bắt đầu cập nhật trạng thái Nguoi dung")
            Log.d(
                "User",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response = BaseRepository(userPreferencesRepository)
                .userRepository.updateUserStatus(_userID.value, _status.value)
            Log.d("User", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                return 1 // Update successful
            } else {
                Log.d("User", "Cập nhật trạng thái thất bại: ${response.errorBody()?.string()}")
                return 0 // Update failed
            }
        } catch (e: Exception) {
            Log.d("User", "Không thể cập nhật trạng thái: ${e.message}")
            return -1 // Error occurred
        } finally {
            _isLoading.value = false
            Log.d("User", "Kết thúc cập nhật trạng thái Nguoi dung")
        }
    }
    fun addUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate a network call or data loading
                // Replace this with your actual data loading logic
                // For example, you can fetch data from a repository or API
                // and update the state variables accordingly.
            } catch (e: Exception) {
                // Handle any errors that occur during data loading
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                ManageUserViewModel(
                    savedStateHandle = savedStateHandle,
                    userPreferencesRepository = application.userPreferencesRepository,
                    imageViewModel = ImageViewModel()
                )
            }
        }
    }
}