package com.example.mam.repository

import com.example.mam.dto.user.UserResponse
import com.example.mam.dto.vo.PageVO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserRepository {
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Long): Response<UserResponse>
    @Multipart
    @PUT("users/{userId}")
    suspend fun updateUser(
        @Path("userId") id: Long,
        @Part("fullname") fullname: RequestBody,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part avatar: MultipartBody.Part? = null,
        @Part("roleId") roleId: RequestBody,
    ): Response<UserResponse>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: String): Response<Unit>

    @PATCH("users/{id}/status")
    suspend fun updateUserStatus(
        @Path("id") userId: Long,
        @Query("status") status: String): Response<Void>

    @POST("users/{id}/assign-role/{roleId}")
    suspend fun assignRoleToUser(
        @Path("id") userId: Long,
        @Path("roleId") roleId: Long): Response<Unit>
    @GET("users")
    suspend fun getAllUsers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String
    ):Response<PageVO<UserResponse>>
}