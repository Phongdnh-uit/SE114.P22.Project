package com.example.mam.services

import com.example.mam.entity.Notification
import com.example.mam.entity.authorization.request.SignInRequest
import com.example.mam.entity.authorization.response.SignInResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIservice {
    @POST("auth/login")
    suspend fun login(@Body request: SignInRequest): SignInResponse
    @POST("auth/register")
    suspend fun signUp(@Body request: SignInRequest)
    @GET("notification")
    suspend fun getNotifications(): List<Notification>
}