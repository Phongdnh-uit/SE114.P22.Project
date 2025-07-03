package com.example.mam.viewmodel.management

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.Constant.BASE_IMAGE
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.repository.retrofit.BaseRepository
import com.example.mam.viewmodel.ImageViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.Instant

class ManageCategoryViewModel(
    savedStateHandle: SavedStateHandle?,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val imageViewModel: ImageViewModel
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val categoryId = savedStateHandle?.get<Long>("categoryId") ?: 0L

    private val _categoryID = MutableStateFlow<Long>(categoryId)
    val categoryID: StateFlow<Long> = _categoryID

    private val _categoryName = MutableStateFlow<String>("")
    val categoryName: StateFlow<String> = _categoryName

    private val _categoryDescription = MutableStateFlow<String>("")
    val categoryDescription: StateFlow<String> = _categoryDescription

    private val _categoryImage = MutableStateFlow<String>(BASE_IMAGE)
    val categoryImage: StateFlow<String> = _categoryImage

    private val _categoryImageFile = MutableStateFlow<File?>(null) // Lưu ảnh dạng Multipart

    private val _createdAt = MutableStateFlow<Instant>(Instant.now())
    val createdAt: StateFlow<Instant> = _createdAt

    private val _updatedAt = MutableStateFlow<Instant>(Instant.now())
    val updatedAt: StateFlow<Instant> = _updatedAt

    fun setCategoryName(name: String) {
        _categoryName.value = name
    }

    fun isCategoryNameValid(): String {
        val name = _categoryName.value.trim()
        val regex = "^[\\p{L} ]+$".toRegex()
        return if (name.length > 20) {
            "Tên quá dài (>20 ký tự)" // Name is too long
        } else if (name.length < 3) {
            "Tên quá ngắn (<3 ký tự)" // Name is too short
        } else if (!regex.matches(name)) {
            "Tên không hợp lệ" // Name contains invalid characters
        } else ""
    }

    fun setCategoryDescription(description: String) {
        _categoryDescription.value = description
    }

    fun isCategoryDescriptionValid(): String {
        val description = _categoryDescription.value.trim()
        return if (description.length > 100) {
            "Mô tả quá dài (>100 ký tự)" // Description is too long
        } else if (description.length < 5) {
            "Mô tả quá ngắn (<5 ký tự)" // Description is too short
        } else ""
    }

    fun setCategoryImage(image: String) {
        _categoryImage.value = image
    }

    fun setCategoryImageFile(context: Context, uri: Uri) {
        val file = imageViewModel.uriToFile(context, uri)
        _categoryImageFile.value = file
        Log.d("Category", "File path: ${file?.absolutePath}")
        Log.d("Category", "File exists: ${file?.exists()}")
        Log.d("Category", "File size: ${file?.length()}")
    }

    suspend fun loadData() {
        _isLoading.value = true
        try {
            Log.d("Category", "Bắt đầu lấy Danh mục")
            Log.d(
                "Category",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response =
                BaseRepository(userPreferencesRepository).productCategoryRepository.getCategory(_categoryID.value)
            Log.d("Category", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val category = response.body()
                if (category != null) {
                    _categoryName.value = category.name
                    _categoryDescription.value = category.description
                    _createdAt.value = Instant.parse(category.createdAt)
                    _updatedAt.value = Instant.parse(category.updatedAt)
                    _categoryImage.value = category.getRealURL()
                    Log.d("Category", _categoryImage.value)
                }

            } else {
                Log.d("Category", "Lấy Danh mục thất bại: ${response.errorBody().toString()}")
            }
        } catch (e: Exception) {
            Log.d("Category", "Không thể lấy Danh mục: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Category", "Kết thúc lấy Danh mục")
        }

    }

    suspend fun updateCategory(): Int {
        _isLoading.value = true
        try {
            Log.d("Category", "Bắt đầu cap nhat Danh mục")
            Log.d(
                "Category",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val namePart = _categoryName.value.toRequestBody("text/plain".toMediaType())
            val descriptionPart = _categoryDescription.value.toRequestBody("text/plain".toMediaType())

            val imageFile = _categoryImageFile.value
            val requestFile = imageFile?.asRequestBody("image/*".toMediaType())
            val imagePart =
                requestFile?.let { MultipartBody.Part.createFormData("image", imageFile.name, it) }

            val response = BaseRepository(userPreferencesRepository).productCategoryRepository.updateCategory(_categoryID.value, namePart, descriptionPart, imagePart)
            Log.d("Category", "${_categoryName.value}, ${_categoryDescription.value}, ${_categoryImageFile.value}")
            Log.d("Category", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val category = response.body()
                if (category != null) {
                    _categoryName.value = category.name
                    _categoryDescription.value = category.description
                    _createdAt.value = Instant.parse(category.createdAt)
                    _updatedAt.value = Instant.parse(category.updatedAt)
                    _categoryImage.value = category.imageUrl
                }
                return 1
            } else {

                Log.d("Category", "Cap nhat Danh mục thất bại: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Category", "Không thể cap nhat Danh mục: ${e.message}")
            return 0
        } finally {
            _isLoading.value = false
            Log.d("Category", "Kết thúc cap nhat Danh mục")
        }
    }

    suspend fun createCategory(): Int {
        _isLoading.value = true
        try {
            Log.d("Category", "Bắt đầu them Danh mục")
            Log.d(
                "Category",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val namePart = _categoryName.value.toRequestBody("text/plain".toMediaType())
            val descriptionPart = _categoryDescription.value.toRequestBody("text/plain".toMediaType())

            val imageFile = _categoryImageFile.value
            val requestFile = imageFile!!.asRequestBody("image/*".toMediaType())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val response = BaseRepository(userPreferencesRepository)
                .productCategoryRepository
                .createCategory(namePart, descriptionPart, imagePart)

            if (response == null){
                return 0
            }
            Log.d("Category", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val category = response.body()
                if (category != null) {
                    _categoryName.value = category.name
                    _categoryDescription.value = category.description
                    _createdAt.value = Instant.parse(category.createdAt)
                    _updatedAt.value = Instant.parse(category.updatedAt)
                    _categoryImage.value = category.imageUrl
                }
                return 1
            } else {
                Log.d("Category", "Them Danh mục thất bại: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Category", "Không thể them Danh mục: ${e.message}")
            return 0
        } finally {
            _isLoading.value = false
            Log.d("Category", "Kết thúc them Danh mục")
        }
    }

    fun mockData() {
        _categoryID.value = 1
        _categoryName.value = "Category Name"
        _categoryDescription.value = "Category Description"
        _categoryImage.value = "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg"
        _createdAt.value = Instant.now()
        _updatedAt.value = Instant.now()
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                ManageCategoryViewModel(
                    savedStateHandle = savedStateHandle,
                    userPreferencesRepository = application.userPreferencesRepository,
                    imageViewModel = ImageViewModel()
                )
            }
        }
    }
}