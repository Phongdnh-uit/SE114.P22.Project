package com.example.mam.repository

import com.example.mam.dto.variation.VariationRequest
import com.example.mam.dto.variation.VariationResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface VariationRepository {
    @PUT("variations/{id}")
    suspend fun updateVariation(
        @Path("id") id: Long,
        @Body request: VariationRequest): Response<VariationResponse>
    @DELETE("variations/{id}")
    suspend fun deleteVariation(@Path("id") id: Long): Response<Unit>
    @POST("variations")
    suspend fun createVariation(@Body variationRequest: VariationRequest): Response<VariationResponse>
    @GET("variations")
    suspend fun getVariationByProduct(
        @Query("productId") productId: Long = 0L,
        @Query("name") name: String ?= null,
        @Query("page") page: Int ?= null,
        @Query("size") size: Int ?= null,
        @Query("sort") sort: List<String>? = null
    ): Response<PageVO<VariationResponse>>
}