package com.example.mam.viewmodel.client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.Constant
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.cart.CartResponse
import com.example.mam.dto.order.OrderRequest
import com.example.mam.dto.payment.VnPayRequest
import com.example.mam.dto.promotion.PromotionResponse
import com.example.mam.dto.user.UserResponse
import com.example.mam.repository.retrofit.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat

class CheckOutViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
):  ViewModel(){
    private val _cart = MutableStateFlow(CartResponse())
    val cart = _cart.asStateFlow()

    private val _total = MutableStateFlow("0 VND")
    val total = _total.asStateFlow()

    private val _orderTotal = MutableStateFlow("0 VND")
    val orderTotal = _orderTotal.asStateFlow()

    private val _user = MutableStateFlow(UserResponse())
    val user = _user.asStateFlow()

    private val _address = MutableStateFlow("")
    val address = _address.asStateFlow()

    private val _latitude = MutableStateFlow(0.0)
    private val _longitude = MutableStateFlow(0.0)

    private val _discount = MutableStateFlow<PromotionResponse?>(null)
    val discount = _discount.asStateFlow()

    private val _discountList = MutableStateFlow<MutableList<PromotionResponse>>(
        mutableListOf(
        )
    )

    val discountList = _discountList.asStateFlow()

    private val _note = MutableStateFlow<String?>(null)
    var note = _note.asStateFlow()

    private val _paymentOptions = MutableStateFlow<MutableList<String>>(mutableListOf())
    val paymentOptions = _paymentOptions.asStateFlow()

    val _paymentOption = MutableStateFlow("") // Default payment option
    val paymentOption = _paymentOption.asStateFlow()

    val _isPaying = MutableStateFlow(false)
    val isPaying = _isPaying.asStateFlow()

    fun setIsPaying(isPaying: Boolean) {
        _isPaying.value = isPaying
        Log.d("CheckOutViewModel", "Payment state updated: $isPaying")
    }

    private fun setTotal(){
        val total = _cart.value.cartItems.sumOf { it.price * it.quantity.toBigDecimal() } - (_discount.value?.discountValue
            ?: BigDecimal.ZERO)
        val format = DecimalFormat("#,###")
        Log.d("CheckOutViewModel", "Total price calculated: $total")
        _orderTotal.value = "${format.format(total)} VND"
    }

    fun setDiscount(code: PromotionResponse?){
        _discount.value = code
        setTotal()
    }

    fun setAddress(address: String) {
        _address.value = address
    }

    fun setLatitude(latitude: Double) {
        _latitude.value = latitude
    }
    fun setLongitude(longitude: Double) {
        _longitude.value = longitude
    }

    fun setAdressAndCoordinates() {
        viewModelScope.launch {
            userPreferencesRepository.saveAddress(_address.value, _longitude.value, _latitude.value)
        }
    }
    fun setupPaymentOption(option: String){
        _paymentOption.value = option
    }

    fun setNote(string: String){
        _note.value = string
    }

    fun loadAddress(){
        viewModelScope.launch {
            if(userPreferencesRepository.address.map { it }.first().isNotEmpty()) _address.value = userPreferencesRepository.address.map { it }.first()
            if(userPreferencesRepository.latitude.map { it }.first() != 0.0) _latitude.value = userPreferencesRepository.latitude.map { it }.first()
            if(userPreferencesRepository.longitude.map { it }.first() != 0.0) _longitude.value = userPreferencesRepository.longitude.map { it }.first()
        }
    }

    suspend fun loadUser() {
        try {
            val userResponse = BaseRepository(userPreferencesRepository).authPrivateRepository.getUserInfo()
            if (userResponse.isSuccessful) {
                _user.value = userResponse.body() ?: UserResponse()
                Log.d("CheckOutViewModel", "User loaded: ${_user.value.username}")
            } else {
                Log.d("CheckOutViewModel", "No user found or error: ${userResponse.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CheckOutViewModel", "Failed to load user: ${e.message}")
        }
    }

    suspend fun loadCart(){
        try {
            Log.d("CartViewModel", "Loading Cart details")
            val response = BaseRepository(userPreferencesRepository).cartRepository.getMyCart()
            Log.d("CartViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                val cartResponse = response.body()
                if (cartResponse != null) {
                    _cart.value = cartResponse
                    _total.value = _cart.value.getTotalPrice()
                    setTotal()
                    Log.d("CartViewModel", "Cart details loaded: ${_cart.value.cartItems.size} items")
                    _cart.value.cartItems.forEach { item ->
                        Log.d("CartViewModel", "Item: ${item.productName}, Quantity: ${item.quantity}, Price: ${item.getPrice()}, Options: ${item.variationOptionInfo}")
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
    }

    suspend fun loadPaymentOptions() {
        try {
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.getMetadata(
                listOf(Constant.metadata.PAYMENT_METHOD.name)
            )
            if (response.isSuccessful) {
                _paymentOptions.value = response.body()?.get(Constant.metadata.PAYMENT_METHOD.name)?.map { it }?.toMutableList() ?: mutableListOf()
                _paymentOption.value = _paymentOptions.value[0]
                Log.d("CheckOutViewModel", "Payment options loaded: ${_paymentOptions.value.size} options")
            } else {
                Log.d("CheckOutViewModel", "Failed to load payment options: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CheckOutViewModel", "Failed to load payment options: ${e.message}")
        }
    }

    suspend fun loadDiscounts() {
        try {
            val response = BaseRepository(userPreferencesRepository).userPromotionRepository.getAvailablePromotionsForOrder(
                _user.value.id,
                _cart.value.cartItems.sumOf { it.price * it.quantity.toBigDecimal() }.toDouble()
            )
            if (response.isSuccessful) {
                _discountList.value = response.body()?.toMutableList() ?: mutableListOf()
                Log.d("CheckOutViewModel", "Discounts loaded: ${_discountList.value.size} promotions")
            } else {
                Log.d("CheckOutViewModel", "Failed to load discounts: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CheckOutViewModel", "Failed to load discounts: ${e.message}")
        }
    }

    suspend fun checkOut(): Int{
        try{
            _isPaying.value = true
            val request = OrderRequest(
                _latitude.value,
                _longitude.value,
                _address.value,
                _note.value,
                _paymentOption.value,
                _discount.value?.id
            )
            Log.d("CheckOutViewModel", "Checking out with request: $request")
            val response = BaseRepository(userPreferencesRepository).orderRepository.createOrder(request)
            Log.d("CheckOutViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("CheckOutViewModel", "Order created successfully")
                return response.body()?.id?.toInt() ?: -1 // Success
            } else {
                Log.d("CheckOutViewModel", "Failed to check out (BE): ${response.errorBody()?.string()}")
                return -1 // Failure
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            Log.d("CheckOutViewModel", "Failed to check out: ${e.message}")
            return -1
        }
        finally {
            _isPaying.value = false
            Log.d("CheckOutViewModel", "Payment state reset to false after checkout")
        }
    }

    suspend fun createPayment(orderId: Int ): String {
        try {
            val request = VnPayRequest(orderId= orderId.toLong(), returnUrl = "mam://payment/result")
            Log.d("CheckOutViewModel", "Creating payment for order ID: $orderId")
            val response = BaseRepository(userPreferencesRepository).paymentRepository.createPayment(request)
            Log.d("CheckOutViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("CheckOutViewModel", "Payment created successfully url: ${response.body()?.get("data")}")
                return response.body()?.get("data") as? String ?: ""
            } else {
                Log.d("CheckOutViewModel", "Failed to create payment (BE): ${response.errorBody()?.string()}")
                return "" // Failure
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CheckOutViewModel", "Failed to create payment: ${e.message}")
            return ""
        }
    }

    suspend fun handlePaymentResult(): Boolean {
        try {
            Log.d("CheckOutViewModel", "Handling payment result")
            val response = BaseRepository(userPreferencesRepository).paymentRepository.returnPayment()
            Log.d("CheckOutViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("CheckOutViewModel", "Payment result handled successfully")
                return true // Success
            } else {
                Log.d("CheckOutViewModel", "Failed to handle payment result (BE): ${response.errorBody()?.string()}")
                return false // Failure
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CheckOutViewModel", "Failed to handle payment result: ${e.message}")
            return false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                CheckOutViewModel(
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }

}