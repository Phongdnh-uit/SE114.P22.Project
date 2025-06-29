package com.example.mam.viewmodel.management

import android.content.Context
import android.icu.util.UniversalTimeScale.toBigDecimal
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import co.yml.charts.common.extensions.isNotNull
import com.example.mam.MAMApplication
import com.example.mam.data.Constant
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.product.CategoryResponse
import com.example.mam.dto.variation.VariationOptionRequest
import com.example.mam.dto.variation.VariationOptionResponse
import com.example.mam.dto.variation.VariationRequest
import com.example.mam.dto.variation.VariationResponse
import com.example.mam.repository.BaseRepository
import com.example.mam.viewmodel.ImageViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.math.BigDecimal
import java.time.Instant

class ManageProductViewModel(
    savedStateHandle: SavedStateHandle?,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val imageViewModel: ImageViewModel
): ViewModel() {
    private val productId: Long = savedStateHandle?.get<Long>("productId")?: 0L

    private val _categoryList =  MutableStateFlow<List<Pair<Long, String>>>(emptyList())
    val categoryList = _categoryList.asStateFlow()

    private val _productID = MutableStateFlow(productId)
    val productID = _productID.asStateFlow()

    private val _createdAt = MutableStateFlow(Instant.now())
    val createdAt = _createdAt.asStateFlow()

    private val _updatedAt = MutableStateFlow(Instant.now())
    val updatedAt = _updatedAt.asStateFlow()

    private val _productName = MutableStateFlow("")
    val productName = _productName.asStateFlow()

    private val _productShortDescription = MutableStateFlow("")
    val productShortDescription = _productShortDescription.asStateFlow()

    private val _productLongDescription = MutableStateFlow("")
    val productLongDescription = _productLongDescription.asStateFlow()

    private val _productPrice = MutableStateFlow("")
    val productPrice = _productPrice.asStateFlow()

    private val _productCategory = MutableStateFlow(0L)
    val productCategory = _productCategory.asStateFlow()

    private val _productImageUrl = MutableStateFlow(Constant.BASE_IMAGE)
    val productImageUrl = _productImageUrl.asStateFlow()

    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile = _imageFile.asStateFlow()

    private val _isAvailable = MutableStateFlow(false)
    val isAvailable = _isAvailable.asStateFlow()

    private val _variants = MutableStateFlow<Map<VariationResponse, List<VariationOptionResponse>>>(emptyMap())
    val variants = _variants.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isSetting = MutableStateFlow(false)
    val isSetting = _isSetting.asStateFlow()

    fun setProductName(name: String) {
        _productName.value = name
    }

    fun isProductNameValid(): String {
        val name = _productName.value.trim()
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

    fun setProductShortDescription(shortDescription: String) {
        _productShortDescription.value = shortDescription
    }

    fun isProductShortDescriptionValid(): String {
        val shortDescription = _productShortDescription.value.trim()
        if (shortDescription.length > 100) {
            return "Mô tả quá dài (>100 ký tự)" // Short description is too long
        }
        else if (shortDescription.length < 10) {
            return "Mô tả quá ngắn (<10 ký tự)" // Short description is too short
        }
        return "" // Short description is valid
    }

    fun setProductLongDescription(longDescription: String) {
        _productLongDescription.value = longDescription
    }

    fun isProductLongDescriptionValid(): String {
        val longDescription = _productLongDescription.value.trim()
        if (longDescription.length > 500) {
            return "Mô tả quá dài (>500 ký tự)" // Long description is too long
        }
        else if (longDescription.length < 20) {
            return "Mô tả quá ngắn (<20 ký tự)" // Long description is too short
        }
        return "" // Long description is valid
    }

    fun setProductPrice(price : String) {
        _productPrice.value = price
    }

    fun setProductImageFile(context: Context, uri: Uri) {
        val file = imageViewModel.uriToFile(context, uri)
        _imageFile.value = file
    }

    fun isProductPriceValid(): String {
        val price = if (_productPrice.value.isNotEmpty()) _productPrice.value.toInt() else 0
        if (price < 1000) {
            return "Giá quá thấp (<1000 VND)" // Price is too low
        }
        else if (price > 1000000) {
            return "Giá quá cao (>1 triệu VND)" // Price is too high
        }
        return ""
    }

    fun setProductCategory(category: Long) {
        _productCategory.value = category
    }

    fun setProductImageUrl(imageUrl: String) {
        _productImageUrl.value = imageUrl
    }

    fun setIsAvailable(available: Boolean) {
        _isAvailable.value = available
    }

    suspend fun addVariant(variant: VariationRequest): Int {
        try {
            val response = BaseRepository(userPreferencesRepository).variationRepository.createVariation(
                variant
            )
            Log.d("ManageProductViewModel", "Add variant response: ${response.code()}")
            if (response.isSuccessful) {
                val newVariant = response.body() ?: return 0
                _variants.value += (newVariant to emptyList())
                return 1
            } else {
                return 0
            }
        }
        catch (e: Exception) {
            Log.e("ManageProductViewModel", "Error adding variant: ${e.message}")
            return 0
        }
    }

    suspend fun updateVariantIsMultipleChoice(variant: VariationResponse):Int  {
        try {
            val response = BaseRepository(userPreferencesRepository).variationRepository.updateVariation(
                variant.id,
                VariationRequest(
                    name = variant.name,
                    isMultipleChoice = !variant.isMultipleChoice,
                    productId = variant.productId
                )
            )
            Log.d("ManageProductViewModel", "Update variant response: ${response.code()}")
            if (response.isSuccessful) {
                _variants.value = _variants.value.mapKeys { (key, value) ->
                    if (key.id == variant.id) {
                        key.copy(isMultipleChoice = !key.isMultipleChoice)
                    } else {
                        key
                    }
                }
                Log.d("ManageProductViewModel", "Variant updated successfully")
                return 1
            }
            else {
                Log.e("ManageProductViewModel", "Failed to update variant: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.e("ManageProductViewModel", "Error updating variant: ${e.message}")
            return 0
        }
    }

    suspend fun addVariantOption(option: VariationOptionRequest): Int {
        try {
            Log.d("ManageProductViewModel", "Adding variant option: $option, variantId: ${option.variationId}")
            val response = BaseRepository(userPreferencesRepository).variationOptionRepository.createVariationOption(
                option
            )
            Log.d("ManageProductViewModel", "Add variant option response: ${response.code()}")
            if (response.isSuccessful) {
                val newOption = response.body() ?: return 0
                _variants.value = _variants.value.map { (variant, options) ->
                    if (variant.id == option.variationId) {
                        variant to (options + newOption)
                    } else {
                        variant to options
                    }
                }.toMap()
                Log.d("ManageProductViewModel", "Variant option added successfully")
                return 1
            } else {
                Log.e("ManageProductViewModel", "Failed to add variant option: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.e("ManageProductViewModel", "Error adding variant option: ${e.message}")
            return 0
        }
    }

    suspend fun removeVariant(id: Long): Int {
        try {
            // 1️⃣ Lấy danh sách option thuộc variant cần xóa
            val optionList = _variants.value[_variants.value.keys.find { it.id == id }] ?: emptyList()

            // 2️⃣ Xóa từng option
            for (option in optionList) {
                val optionResponse = BaseRepository(userPreferencesRepository)
                    .variationOptionRepository.deleteVariationOption(option.id)

                if (!optionResponse.isSuccessful) {
                    Log.e("ManageProductViewModel", "Failed to remove variant option ${option.id}: ${optionResponse.errorBody()?.string()}")
                    return 0
                }
            }

            // 3️⃣ Sau khi xóa hết option → xóa variant
            val response = BaseRepository(userPreferencesRepository)
                .variationRepository.deleteVariation(id)

            Log.d("ManageProductViewModel", "Remove variant response: ${response.code()}")
            if (response.isSuccessful) {
                _variants.value = _variants.value.filterKeys { it.id != id }
                Log.d("ManageProductViewModel", "Variant removed successfully")
                return 1
            } else {
                Log.e("ManageProductViewModel", "Failed to remove variant: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.e("ManageProductViewModel", "Error removing variant: ${e.message}", e)
            return 0
        }

    }

    suspend fun removeVariantOption(id: Long): Int {
        try {
            val response = BaseRepository(userPreferencesRepository).variationOptionRepository.deleteVariationOption(id)
            Log.d("ManageProductViewModel", "Remove variant option response: ${response.code()}")
            if (response.isSuccessful) {
                _variants.value = _variants.value.mapValues { options ->
                    options.value.filter { it.id != id }
                }
                Log.d("ManageProductViewModel", "Variant option removed successfully")
                return 1
            } else {
                Log.e("ManageProductViewModel", "Failed to remove variant option: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.e("ManageProductViewModel", "Error removing variant option: ${e.message}")
            return 0
        }
    }

    suspend fun setCategoryList() {
        var currentPage = 0
        val allCategories = mutableListOf<CategoryResponse>()
        try {
            Log.d("Category", "Bắt đầu lấy Danh mục")

            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .productCategoryRepository
                    .getCategories(filter = "", page = currentPage)

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
                        _categoryList.value = allCategories.map { it.id to it.name } // Update UI with current categories
                        Log.d("Category", "Danh mục hiện tại: ${_categoryList.value.size} danh mục")
                        Log.d("Category", "Danh mục hiện tại: ${_categoryList.value.joinToString(", ") { "${it.first}: ${it.second}" }}")
                    }
                    else break
                } else {
                    Log.d("Category", "Lấy Danh mục thất bại: ${response.errorBody()}")
                    break // Stop loop on failure
                }
            }

            _categoryList.value = allCategories.map { it.id to it.name }// Update UI with all categories

        } catch (e: Exception) {
            Log.d("Category", "Không thể lấy Danh mục: ${e.message}")
        }
    }

    suspend fun loadData() {
            try {
                _isLoading.value = true
                if (productId != 0L) {
                    // Simulate network call to fetch product data
                    val response = BaseRepository(userPreferencesRepository).productRepository.getProductById(productId.toLong())
                    Log.d("ManageProductViewModel", "Load product response: ${response.code()}")
                    if (response.isSuccessful) {
                        val product = response.body()
                        if (product != null) {
                            _productID.value = product.id
                            _createdAt.value = Instant.parse(product.createdAt)
                            _updatedAt.value = Instant.parse(product.updatedAt)
                            _productName.value = product.name
                            _productShortDescription.value = product.shortDescription
                            _productLongDescription.value = product.detailDescription
                            _productPrice.value = product.originalPrice.toString()
                            _productCategory.value = product.categoryId
                            _productImageUrl.value = product.getRealURL() ?: Constant.BASE_IMAGE
                            _isAvailable.value = product.isAvailable
                            Log.d("ManageProductViewModel", "Product loaded successfully: ${product.name}, ${productCategory.value}")
                            // Load variants if available
                            val variantResponse = BaseRepository(userPreferencesRepository).variationRepository.getVariationByProduct(product.id, page = 0, size = 100)
                            if (variantResponse.isSuccessful) {
                                val variants = variantResponse.body()?.content ?: emptyList()
                                val variantOptions = mutableMapOf<VariationResponse, List<VariationOptionResponse>>()

                                for (variant in variants) {
                                    val optionsResponse = BaseRepository(userPreferencesRepository).variationOptionRepository.getVariationOption(variant.id, page = 0, size = 100)
                                    if (optionsResponse.isSuccessful) {
                                        variantOptions[variant] = optionsResponse.body()?.content ?: emptyList()
                                    } else {
                                        Log.e("ManageProductViewModel", "Failed to load variant options: ${optionsResponse.errorBody()?.string()}")
                                    }
                                }
                                _variants.value = variantOptions
                            } else {
                                Log.e("ManageProductViewModel", "Failed to load variants: ${variantResponse.errorBody()?.string()}")
                            }
                        }
                    } else {
                        Log.e("ManageProductViewModel", "Failed to load product: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("ManageProductViewModel", "Error loading product data: ${e.message}")
            } finally {
                _isLoading.value = false
            }

    }

    suspend fun addProduct(): Int {
        try {
            _isSetting.value = true
            val name = _productName.value.toRequestBody("text/plain".toMediaTypeOrNull())
            val shortDescription = _productShortDescription.value.toRequestBody("text/plain".toMediaTypeOrNull())
            val longDescription = _productLongDescription.value.toRequestBody("text/plain".toMediaTypeOrNull())
            val price = _productPrice.value.toRequestBody("text/plain".toMediaTypeOrNull())
            val categoryId = _productCategory.value.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val imageFile = _imageFile.value
            if (!imageFile.isNotNull()) return 0
            val requestFile = imageFile!!.asRequestBody("image/*".toMediaType())
            val imagePart =  MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val response = BaseRepository(userPreferencesRepository).productRepository.createProduct(
                name = name,
                shortDescription = shortDescription,
                detailDescription = longDescription,
                originalPrice = price,
                categoryId = categoryId,
                image = imagePart
            )
            Log.d("ManageProductViewModel", "Add product response: ${response.code()}")
            if (response.isSuccessful) {
                val product = response.body()
                if (product != null) {
                    _productID.value = product.id
                    _createdAt.value = Instant.parse(product.createdAt)
                    _updatedAt.value = Instant.parse(product.updatedAt)
                    _productName.value = product.name
                    _productShortDescription.value = product.shortDescription
                    _productLongDescription.value = product.detailDescription
                    _productPrice.value = product.originalPrice.toString()
                    _productCategory.value = product.categoryId
                    _productImageUrl.value = product.getRealURL() ?: Constant.BASE_IMAGE
                    _isAvailable.value = product.isAvailable
                }
                return 1
            } else {
                Log.e(
                    "ManageProductViewModel",
                    "Failed to add product: ${response.errorBody()?.string()}"
                )
                return 0
            }
        } catch (e: Exception) {
            Log.e("ManageProductViewModel", "Error adding product: ${e}")
            return 0
        } finally {
            _isSetting.value = false
        }
    }

    suspend fun updateProduct():Int {
        try {
            _isSetting.value = true
            val name = _productName.value.toRequestBody("text/plain".toMediaTypeOrNull())
            val shortDescription = _productShortDescription.value.toRequestBody("text/plain".toMediaTypeOrNull())
            val longDescription = _productLongDescription.value.toRequestBody("text/plain".toMediaTypeOrNull())
            val price = _productPrice.value.toRequestBody("text/plain".toMediaTypeOrNull())
            val categoryId = _productCategory.value.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val imageFile = _imageFile.value
            val requestFile = imageFile?.asRequestBody("image/*".toMediaType())
            val imagePart = requestFile?.let{ MultipartBody.Part.createFormData("image", imageFile.name, requestFile)}
            val response = BaseRepository(userPreferencesRepository).productRepository.updateProduct(
                id = _productID.value,
                name = name,
                shortDescription = shortDescription,
                detailDescription = longDescription,
                originalPrice = price,
                categoryId = categoryId,
                image = imagePart
            )
            Log.d("ManageProductViewModel", "Update product response: ${response.code()}")
            if (response.isSuccessful) {
                val product = response.body()
                if (product != null) {
                    _productID.value = product.id
                    _createdAt.value = Instant.parse(product.createdAt)
                    _updatedAt.value = Instant.parse(product.updatedAt)
                    _productName.value = product.name
                    _productShortDescription.value = product.shortDescription
                    _productLongDescription.value = product.detailDescription
                    _productPrice.value = product.originalPrice.toString()
                    _productCategory.value = product.categoryId
                    _productImageUrl.value = product.getRealURL() ?: Constant.BASE_IMAGE
                    _isAvailable.value = product.isAvailable
                }
                return 1
            } else {
                Log.e("ManageProductViewModel", "Update failed: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.e("ManageProductViewModel", "Error updating product: ${e.message}")
            return 0
        } finally {
            _isSetting.value = false
        }
    }

    fun mockData(){

    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                ManageProductViewModel(
                    savedStateHandle = savedStateHandle,
                    userPreferencesRepository = application.userPreferencesRepository,
                    imageViewModel = ImageViewModel()
                )
            }
        }
    }
}