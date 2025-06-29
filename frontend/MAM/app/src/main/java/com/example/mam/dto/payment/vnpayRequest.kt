package com.example.mam.dto.payment

data class VnPayRequest (
    val orderId: Long = 0L,
    val orderInfo: String? = orderId.toString(),
    val orderType: String? = "billpayment",
    val bankCode: String? = "NCB",
    val language: String? = "vn",

    val billingMobile: String? = null,
    val billingEmail: String? = null,
    val billingFullname: String? = null,
    val billingAddress: String? = null,
    val billingCity: String? = null,
    val billingCountry: String? = null,
    val billingState: String? = null,

    val invMobile: String? = null,
    val invEmail: String? = null,
    val invCustomer: String? = null,
    val invAddress: String? = null,
    val invCompany: String? = null,
    val invTaxCode: String? = null,
    val invType: String? = null,

    val returnUrl : String,
)
