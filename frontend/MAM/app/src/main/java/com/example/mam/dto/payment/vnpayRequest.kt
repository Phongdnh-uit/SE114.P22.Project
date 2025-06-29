package com.example.mam.dto.payment

data class VnPayRequestDTO (
    val orderId: Long = 0L,
    val orderInfo: String? = null,
    val orderType: String? = null,
    val txnRef: String? = null,
    val bankCode: String? = null,
    val language: String? = null,

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
)
