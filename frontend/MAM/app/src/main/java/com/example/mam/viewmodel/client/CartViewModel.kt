package com.example.mam.viewmodel.client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.cart.CartResponse
import com.example.mam.dto.cart.CartItemRequest
import com.example.mam.dto.cart.CartItemResponse
import com.example.mam.dto.product.ProductResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _cart = MutableStateFlow(CartResponse())
    val cart = _cart.asStateFlow()

    private val _recommendedProducts = MutableStateFlow<List<ProductResponse>>(emptyList())
    val recommendedProducts = _recommendedProducts.asStateFlow()

    private val _total = MutableStateFlow("0 VND")
    val total = _total.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    suspend fun getCart(){
        try {
            _isLoading.value = true
            Log.d("CartViewModel", "Loading Cart details")
            val response = BaseRepository(userPreferencesRepository).cartRepository.getMyCart()
            Log.d("CartViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                val cartResponse = response.body()
                if (cartResponse != null) {
                    _cart.value = cartResponse
                    _total.value = _cart.value.getTotalPrice()
                    Log.d("CartViewModel", "Cart details loaded: ${_cart.value.cartItems.size} items")
                    _cart.value.cartItems.forEach { item ->
                        Log.d("CartViewModel", "Item: ${item.productName}, Quantity: ${item.quantity}, Price: ${item.getPrice()}, Options: ${item.variationOptionInfo}, Available: ${item.available}")
                    }

                } else {
                    Log.d("CartViewModel", "No cart found")
                }
            } else {
                Log.d("CartViewModel", "Failed to load cart: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CartViewModel", "Failed to load cart: ${e.message}")
        }
        finally {
            _isLoading.value = false
        }
    }

    suspend fun loadAdditionalProduct(){
        try {
            val response = BaseRepository(userPreferencesRepository).productRepository.getRecommendedProducts()
            Log.d("CartViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                val products = response.body()
                if (products != null) {
                    _recommendedProducts.value = products
                    Log.d("CartViewModel", "Recommended products loaded: ${_recommendedProducts.value.size} items")
                } else {
                    Log.d("CartViewModel", "No recommended products found")
                }
            } else {
                Log.d("CartViewModel", "Failed to load recommended products: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CartViewModel", "Failed to load additional products: ${e.message}")
        }
    }

    suspend fun incrItemQuantity(item: CartItemResponse){
        try {
            val response = BaseRepository(userPreferencesRepository).cartItemRepository.updateCartItem(
                id = item.id,
                cartItemRequest = CartItemRequest(
                    productId = item.productId,
                    quantity = item.quantity + 1,
                    variationOptionIds = item.variationOptionIds
                )
            )
            Log.d("CartViewModel", "Increasing item quantity for ID: ${item.id}")
            Log.d("CartViewModel", "Product ID: ${item.productId}, Current Quantity: ${item.quantity}, New Quantity: ${item.quantity + 1}, Variation Options: ${item.variationOptionIds}")
            Log.d("CartViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful){
                _cart.value.cartItems.find { it.id == item.id }?.let {
                    it.quantity += 1
                    _total.value = _cart.value.getTotalPrice()
                }
            }
            else {
                Log.d("CartViewModel", "Failed to increase item quantity: ${response.errorBody()?.string()}")
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            Log.d("CartViewModel", "Failed to increase item quantity: ${e.message}")
        }

    }

    suspend fun descItemQuantity(item: CartItemResponse){
        try {
            val response = BaseRepository(userPreferencesRepository).cartItemRepository.updateCartItem(
                id = item.id,
                cartItemRequest = CartItemRequest(
                    productId = item.productId,
                    quantity = if (item.quantity > 1) item.quantity - 1 else 1,
                    variationOptionIds = item.variationOptionIds
                )
            )
            Log.d("CartViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful){
                _cart.value.cartItems.find { it.id == item.id }?.let {
                    if (it.quantity > 1) {
                        it.quantity -= 1
                    }
                    _total.value = _cart.value.getTotalPrice()
                }
                Log.d("CartViewModel", "Item quantity decreased successfully")
            }
            else {
                Log.d("CartViewModel", "Failed to decrease item quantity: ${response.errorBody()?.string()}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CartViewModel", "Failed to decrease item quantity: ${e.message}")
        }

    }

    suspend fun deleteItem(id: Long){
        try {
            Log.d("CartViewModel", "Deleting item with ID: $id")
            val response = BaseRepository(userPreferencesRepository).cartItemRepository.deleteCartItem(id)
            Log.d("CartViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("CartViewModel", "Item deleted successfully")
                _cart.value.cartItems = _cart.value.cartItems.filter { it.id != id }
                _total.value = _cart.value.getTotalPrice()
            } else {
                Log.d("CartViewModel", "Failed to delete item: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CartViewModel", "Failed to delete item: ${e.message}")
        }

    }

//    suspend fun getProductById(id: Long): String {
//        try {
//            Log.d("CartViewModel", "Loading Cart details for ID: $id")
//            val response = BaseService(userPreferencesRepository).productService.getProductById(
//                id = _itemID
//            )
//            Log.d("ItemViewModel", "Response Code: ${response.code()}")
//            if (response.isSuccessful) {
//                val product = response.body()
//                if (product != null) {
//                    _item.value = product
//                    Log.d("CartViewModel", "Cart details loaded: ${_item.value.name}")
//                } else {
//                    Log.d("CartViewModel", "No item found for ID: $_itemID")
//                }
//            } else {
//                Log.d("CartViewModel", "Failed to load item details: ${response.errorBody()?.string()}")
//            }
//        }
//        catch (e: Exception) {
//            Log.d("CartViewModel", "Error loading item details: ${e.message}")
//            e.printStackTrace()
//        }
//    }
//
//    suspend fun getOptionById(id: Long): String {
//        return try {
//            // Simulate fetching option by ID from a repository or service
//            // Replace with actual data fetching logic
//            null // Replace with actual option data
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.d("CartViewModel", "Failed to load option by ID: ${e.message}")
//            null
//        }
//    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                CartViewModel(
                    application.userPreferencesRepository)
            }
        }
    }
}