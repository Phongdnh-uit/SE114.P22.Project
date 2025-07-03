package com.example.mam.repository

import com.example.mam.dto.cart.CartItemRequest
import com.example.mam.dto.cart.CartItemResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartItemRepository {
    @POST("cart-items")
    suspend fun addCartItem(
        @Body cartItemRequest: CartItemRequest
    ): Response<CartItemResponse>
    @PUT("cart-items/{id}")
    suspend fun updateCartItem(
        @Body cartItemRequest: CartItemRequest,
        @Path("id") id: Long
    ): Response<CartItemResponse>
    @DELETE("cart-items/{id}")
    suspend fun deleteCartItem(
        @Path("id") id: Long
    ): Response<Void>
}