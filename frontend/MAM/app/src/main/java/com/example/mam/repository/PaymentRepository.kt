package com.example.mam.repository

import com.example.mam.dto.payment.VnPayRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentRepository {
    @POST("payment/create-payment")
    suspend fun createPayment(@Body payRequest: VnPayRequest): Response<Map<String, Any>>
    @GET("payment/return")
    suspend fun returnPayment(): Response<Void>
}