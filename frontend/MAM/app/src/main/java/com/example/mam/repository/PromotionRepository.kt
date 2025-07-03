package com.example.mam.repository

import com.example.mam.dto.promotion.PromotionRequest
import com.example.mam.dto.promotion.PromotionResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PromotionRepository {
    @GET("promotions/{id}")
    suspend fun getPromotion(@Path("id") id: Long): Response<PromotionResponse>
    @PUT("promotions/{id}")
    suspend fun updatePromotion(
        @Path("id") id: Long,
        @Body promotionRequest: PromotionRequest
    ): Response<PromotionResponse>
    @DELETE("promotions/{id}")
    suspend fun deletePromotion(@Path("id") id: Long): Response<Void>
    @GET("promotions")
    suspend fun getAllPromotions(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String
    ): Response<PageVO<PromotionResponse>>
    @POST("promotions")
    suspend fun createPromotion(@Body promotionRequest: PromotionRequest): Response<PromotionResponse>
}