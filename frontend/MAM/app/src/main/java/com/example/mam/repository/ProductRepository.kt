package com.example.mam.repository

import com.example.mam.dto.product.ProductResponse
import com.example.mam.dto.vo.PageVO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductRepository {
    @Multipart
    @POST("products")
    suspend fun createProduct(
        @Part("categoryId") categoryId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("shortDescription") shortDescription: RequestBody,
        @Part("detailDescription") detailDescription: RequestBody,
        @Part("originalPrice") originalPrice: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Response<ProductResponse>

    @Multipart
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Part("categoryId") categoryId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("shortDescription") shortDescription: RequestBody,
        @Part("detailDescription") detailDescription: RequestBody,
        @Part("originalPrice") originalPrice: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ProductResponse>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Void>

    @GET("products/category/{id}")
    suspend fun getProductsByCategory(
        @Path("id") id: Long,
        @Query("page") page: Int ,
        @Query("size") size: Int?=null,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String): Response<PageVO<ProductResponse>>

    @GET("products")
    suspend fun getAllProducts(
        @Query("page") page: Int,
        @Query("size") size: Int? = null,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String): Response<PageVO<ProductResponse>>

    @GET("products/recommended")
    suspend fun getRecommendedProducts(): Response<List<ProductResponse>>
}