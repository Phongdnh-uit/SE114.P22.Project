package com.example.mam.viewmodel.client

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
import com.example.mam.dto.cart.CartItemRequest
import com.example.mam.dto.product.ProductResponse
import com.example.mam.dto.variation.VariationOptionResponse
import com.example.mam.dto.variation.VariationResponse
import com.example.mam.repository.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.DecimalFormat


class ItemViewModel(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _itemID = savedStateHandle.get<Long>("itemId")
        ?: throw IllegalArgumentException("Item ID is required")

    private val _item = MutableStateFlow(ProductResponse())
    val item = _item.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity = _quantity.asStateFlow()

    private val _variances = MutableStateFlow<List<VariationResponse>>(emptyList())
    val variances = _variances.asStateFlow()

    private val _optionsMap = MutableStateFlow<Map<Long, List<VariationOptionResponse>>>(emptyMap())
    val optionsMap = _optionsMap.asStateFlow()

    private val _selectedOptions = MutableStateFlow<List<VariationOptionResponse>>(emptyList())
    val selectedOptions = _selectedOptions.asStateFlow()

    private val _cartCount = MutableStateFlow<Int>(0)
    val cartCount = _cartCount.asStateFlow()

    suspend fun loadCartCount() {
        try {
            val response = BaseRepository(userPreferencesRepository).cartRepository.getMyCartCount()
            Log.d("ItemViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                val count = response.body()
                if (count != null) {
                    _cartCount.value = count
                    Log.d("ItemViewModel", "Cart count loaded: $count")
                } else {
                    Log.d("ItemViewModel", "No cart found")
                }
            } else {
                Log.d("ItemViewModel", "Failed to load cart count: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("ItemViewModel", "Failed to load cart count: ${e.message}")
        }
    }

    fun setQuantity(newQuantity: Int) {
        if (newQuantity > 0) {
            _quantity.value = newQuantity
        }
    }

    fun getTotalPrice(): String {
        val basePrice = _item.value.originalPrice
        val additionalPrice = _selectedOptions.value.sumOf { it.additionalPrice }
        val total = (basePrice.plus(additionalPrice.toBigDecimal())) * _quantity.value.toBigDecimal()
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(total)} VND"
    }

    fun selectRatioOption(option : VariationOptionResponse) {
        _selectedOptions.value = _selectedOptions.value.toMutableList().apply {
            removeAll { it.variationId == option.variationId }
            add(option)
        }
        getTotalPrice()
        Log.d("ItemViewModel", "Selected options updated: ${_selectedOptions.value.map { it.value }}")
    }
    fun selectOption(optionId: VariationOptionResponse) {
       _selectedOptions.value = _selectedOptions.value.toMutableList().apply {
            add(optionId)
        }
        getTotalPrice()
        Log.d("ItemViewModel", "Selected options updated: ${_selectedOptions.value.map { it.value }}")
    }

    fun deselectOption(optionId: VariationOptionResponse) {
        _selectedOptions.value = _selectedOptions.value.toMutableList().apply {
            remove(optionId)
        }
        getTotalPrice()
        Log.d("ItemViewModel", "Selected options updated: ${_selectedOptions.value.map { it.value }}")
    }

    suspend fun loadItemDetails() {
        try {
            Log.d("ItemViewModel", "Loading item details for ID: $_itemID")
            val response = BaseRepository(userPreferencesRepository).productRepository.getProductById(
                id = _itemID
            )
            Log.d("ItemViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                val product = response.body()
                if (product != null) {
                    _item.value = product
                    Log.d("ItemViewModel", "Item details loaded: ${_item.value.name}")
                } else {
                    Log.d("ItemViewModel", "No item found for ID: $_itemID")
                }
            } else {
                Log.d("ItemViewModel", "Failed to load item details: ${response.errorBody()?.string()}")
            }
        }
        catch (e: Exception) {
            Log.d("ItemViewModel", "Error loading item details: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun loadVariances() {
        try {
            Log.d("ItemViewModel", "Loading variances for product ID: $_itemID")
           val response = BaseRepository(userPreferencesRepository).variationRepository.getVariationByProduct(
               productId = _itemID
           )
            Log.d("ItemViewModel", "Reponse Code: ${response.code()}")
            if (response.isSuccessful){
                val page = response.body()
                if (page != null) {
                    _variances.value = page.content
                    Log.d("ItemViewModel", "Variances loaded: ${_variances.value.size}")
                } else {
                    Log.d("ItemViewModel", "No variances found for product ID: $_itemID")
                }
            } else {
                Log.d("ItemViewModel", "Failed to load variances: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("ItemViewModel", "Error loading variances: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun loadOptions() {
        try {
            Log.d("ItemViewModel", "Loading options for variances")
            val optionsMap = mutableMapOf<Long, List<VariationOptionResponse>>()
            for (variance in _variances.value) {
                val response = BaseRepository(userPreferencesRepository).variationOptionRepository.getVariationOption(
                    variationId = variance.id
                )
                if (response.isSuccessful) {
                    val options = response.body()
                    if (options != null) {
                        optionsMap[variance.id] = options.content
                        Log.d("ItemViewModel", "Loaded ${options.size} options for variance ID: ${variance.id}")
                    }
                    else {
                        optionsMap[variance.id] = emptyList()
                        Log.d("ItemViewModel", "No options found for variance ID: ${variance.id}")
                    }

                } else {
                    Log.d("ItemViewModel", "Failed to load options for variance ID: ${variance.id}, Error: ${response.errorBody()?.string()}")
                }
            }
            _optionsMap.value = optionsMap
        } catch (e: Exception) {
            Log.d("ItemViewModel", "Error loading options: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun addToCart(): Int{
        try {
            val basePrice = _item.value.originalPrice
            val additionalPrice = _selectedOptions.value.sumOf { it.additionalPrice }
            Log.d("ItemViewModel", "Adding item to cart: ${_item.value.name}, Quantity: ${_quantity.value}")
            val request = CartItemRequest(
                productId = _item.value.id,
                quantity = _quantity.value.toLong(),
                variationOptionIds = _selectedOptions.value.map { it.id }.toSet()
            )
            Log.d("ItemViewModel", "CartItemRequest: $request")
            val response = BaseRepository(userPreferencesRepository).cartItemRepository.addCartItem(request)
            Log.d("ItemViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("ItemViewModel", "Item added to cart successfully")
                return 1
            } else {
                Log.d("ItemViewModel", "Failed to add item to cart: ${response.errorBody()?.string()}")
                return 0
            }
        }
        catch (e: Exception) {
            Log.d("ItemViewModel", "Error adding item to cart: ${e.message}")
            e.printStackTrace()
            return 0
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ItemViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    application.userPreferencesRepository)
            }
        }
    }
}