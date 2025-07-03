package com.example.mam.repository

import com.example.mam.dto.promotion.PromotionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserPromotionRepository {
    @POST("user-promotions/use")
    suspend fun markPromotionAsUsed(
        @Query("userId") userId: Long,
        @Query("promotionId") promotionId: Long
    ): Response<Unit>
    @GET("user-promotions/available")
    suspend fun getAvailablePromotions(
        @Query("userId") userId: Long
    ): Response<List<PromotionResponse>>
    @GET("user-promotions/available-for-order")
    suspend fun getAvailablePromotionsForOrder(
        @Query("userId") userId: Long,
        @Query("orderValue") orderValue: Double,
    ): Response<List<PromotionResponse>>
}