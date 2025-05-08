package com.example.mam.viewmodel.client

import androidx.lifecycle.ViewModel
import com.example.mam.R
import com.example.mam.entity.Shipper
import com.example.mam.entity.OrderItem
import com.example.mam.entity.PaymentType
import com.example.mam.entity.Product
import com.example.mam.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.DecimalFormat

class OrderViewModel(): ViewModel() {
    private val _items = MutableStateFlow<MutableList<OrderItem>>(mutableListOf())
    val items = _items.asStateFlow()

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    var address: String = "Hàn Thuyên, khu phố 6 P, Thủ Đức, Hồ Chí Minh."

    private val _discount = MutableStateFlow(0)
    var discount = _discount.asStateFlow()

    private val _note = MutableStateFlow("")
    var note = _note.asStateFlow()

    private val _status = MutableStateFlow(0)
    var status = _status.asStateFlow()

    private val _paymentType = MutableStateFlow(PaymentType())
    var paymentType = _paymentType.asStateFlow()

    private val _totalPrice = MutableStateFlow(0)
    var totalPrice  = _totalPrice.asStateFlow()

    private val _shipper = MutableStateFlow(Shipper())
    var shipper =_shipper.asStateFlow()

    fun getUser(): User{
        return _user.value
    }

    fun getShipper(): Shipper{
        return _shipper.value
    }
    fun getDiscountToString(): String {
        return getPriceToString(_discount.value)
    }


    fun getTotalToString(): String {
        return getPriceToString(_totalPrice.value)
    }

    fun getPaymentType(): String {
        return _paymentType.value.name
    }
    fun getPriceToString(price: Int): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(price)} VND"
    }

    fun loadOrder(){
        _user.value = User("001", "Đinh Thanh Tùng", "0904599204", "dinhthanhtung0312@gmail.com","ThanhTungDepTrai", "123456")
        _paymentType.value = PaymentType("PT001", "Tiền mặt" )
        _discount.value = 20000
        _note.value = "Lấy thêm tương ớt"
        _items.value =mutableListOf(
            OrderItem(
                product = Product("P003", "Pizza truyền thống", "", "", 120000, true, "PC001", R.drawable.bacon_and_cheese_heaven),
                quantity = 2,
                options = "25cm, Hành tây",
                price = 120000 * 2 + 10000 + 5000
            ),
            OrderItem(
                product = Product("P006", "Hotdog truyền thống", "", "", 80000, true, "PC003", R.drawable.bacon_and_cheese_heaven),
                quantity = 1,
                options = "30cm",
                price = 80000 + 15000
            )
        )
        _totalPrice.value = 345000
        _shipper.value = Shipper("Nguyễn Văn A", "0904599202", "59-G2 123.45")
    }
}