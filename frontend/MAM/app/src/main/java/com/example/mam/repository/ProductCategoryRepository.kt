package com.example.mam.repository

import com.example.mam.dto.product.CategoryResponse
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

interface ProductCategoryRepository {
 @GET("product-categories/{id}")
 suspend fun getCategory(@Path("id") id: Long): Response<CategoryResponse>

 @GET("product-categories")
 suspend fun getCategories(
     @Query("page") page: Int = 0,
     @Query("size") size: Int = 20,
     @Query("sort") sort: List<String>? = null,
     @Query("filter") filter: String
 ): Response<PageVO<CategoryResponse>>

 @Multipart
 @PUT("product-categories/{id}")
 suspend fun updateCategory(
     @Path("id") id: Long,
     @Part("name") name: RequestBody,
     @Part("description") description: RequestBody?,
     @Part image: MultipartBody.Part?
 ): Response<CategoryResponse>

@Multipart
@POST("product-categories")
suspend fun createCategory(
    @Part("name") name: RequestBody,
    @Part("description") description: RequestBody?,
    @Part image: MultipartBody.Part
): Response<CategoryResponse>

 @DELETE("product-categories/{id}")
 suspend fun deleteCategory(@Path("id") id: Long): Response<Void>
}