package com.example.mam.services

import com.example.mam.entity.Notification
import com.example.mam.entity.User
import com.example.mam.entity.authorization.request.SignInRequest
import com.example.mam.entity.authorization.response.SignInResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface APIservice {
    @POST("auth/login")
    suspend fun login(@Body request: SignInRequest): SignInResponse
    @POST("auth/register")
    suspend fun signUp(@Body request: SignInRequest)
    @GET("notification")
    suspend fun getNotifications(): List<Notification>
    @GET("user/profile")
    suspend fun getUser(): User
    @Multipart
    @PUT("user/avatar")
    suspend fun uploadAvatar(
        @Part avatar: MultipartBody.Part
    ): User
}