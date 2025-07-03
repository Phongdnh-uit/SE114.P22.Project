package com.example.mam.repository

import com.example.mam.dto.variation.VariationOptionRequest
import com.example.mam.dto.variation.VariationOptionResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface VariationOptionRepository {
    @PUT("variation-options/{id}")
    suspend fun updateVariationOption(@Path("id") id: Long): Response<VariationOptionResponse>
    @DELETE("variation-options/{id}")
    suspend fun deleteVariationOption(@Path("id") id: Long): Response<Unit>
    @GET("variation-options")
    suspend fun getVariationOption(
        @Query("variationId") variationId: Long,
        @Query("page") page: Int ?= null,
        @Query("size") size: Int ?= null,
        @Query("sort") sort: List<String>? = null,
    ): Response<PageVO<VariationOptionResponse>>
    @POST("variation-options")
    suspend fun createVariationOption(
        @Body variationOptionRequest: VariationOptionRequest
    ): Response<VariationOptionResponse>
}