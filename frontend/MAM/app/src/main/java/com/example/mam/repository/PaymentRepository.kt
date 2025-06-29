package com.example.mam.repository

import com.example.mam.dto.payment.VnPayRequestDTO
import retrofit2.Response
import retrofit2.http.POST

interface PaymentRepository {
    @POST("payment/create-payment")
    suspend fun createPayment(payRequestDTO: VnPayRequestDTO): Response<Map<String, Any>>
}