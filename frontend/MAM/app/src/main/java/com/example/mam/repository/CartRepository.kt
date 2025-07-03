package com.example.mam.repository

import com.example.mam.dto.cart.CartResponse
import retrofit2.Response
import retrofit2.http.GET

interface CartRepository {
    @GET("carts/me")
    suspend fun getMyCart(): Response<CartResponse>
    @GET("carts/me/count")
    suspend fun getMyCartCount(): Response<Int>

}