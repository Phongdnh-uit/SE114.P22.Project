package com.example.mam.services

import com.example.mam.services.request.SignInRequest
import com.example.mam.services.response.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface APIservice {
    @POST("auth/login")
    suspend fun login(@Body request: SignInRequest): SignInResponse
}