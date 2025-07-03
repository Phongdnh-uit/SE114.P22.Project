package com.example.mam.repository

import com.example.mam.dto.authentication.ChangePasswordRequest
import com.example.mam.dto.authentication.RefreshTokenRequest
import com.example.mam.dto.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthPrivateRepository {
    @GET("auth/me")
    suspend fun getUserInfo(): Response<UserResponse>
    @POST("auth/logout")
    suspend fun logOut(@Body refreshToken: RefreshTokenRequest): Response<Void>
    @POST("auth/change-password")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<Void>
}