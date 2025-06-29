package com.example.mam.repository

import com.example.mam.dto.order.OrderRequest
import com.example.mam.dto.order.OrderResponse
import com.example.mam.dto.review.ReviewRequest
import com.example.mam.dto.review.ReviewResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderRepository {
    @PUT("orders/update/{id}")
    suspend fun updateOrderStatus(
        @Path("id") id: Long,
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>
    @POST("orders/create")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<OrderResponse>
    @POST("orders/cancel/{id}")
    suspend fun cancelOrder(@Path("id") id: Long): Response<Void>
    @GET("orders")
    suspend fun getAllOrders(
        @Query("page") page: Int = 0,
        @Query("size") size: Int ? = null,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String
    ): Response<PageVO<OrderResponse>>
    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") id: Long): Response<OrderResponse>
    @GET("orders/me")
    suspend fun getMyOrders(
        @Query("page") page: Int = 0,
        @Query("size") size: Int ? = null,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String
    ): Response<PageVO<OrderResponse>>
    @PUT("orders/{orderId}/delivered")
    suspend fun markOrderAsDelivered(
        @Path("orderId") orderId: Long
    ): Response<Void>
    @GET("orders/{orderId}/status")
    suspend fun getOrderStatus(
        @Path("orderId") orderId: Long,
        @Query("status") status: String
    ): Response<Void>
    @GET("orders/delete/{id}")
    suspend fun deleteOrder(@Path("id") id: String): Response<OrderResponse>

    @POST("reviews")
    suspend fun createReview(@Body reviewRequest: ReviewRequest): Response<ReviewResponse>

    @PUT("reviews/{reviewId}")
    suspend fun updateReview(
        @Path("reviewId") reviewId: Long,
        @Body reviewRequest: ReviewRequest
    ): Response<ReviewResponse>

    @GET("reviews/order/{orderId}")
    suspend fun getReviewByOrderId(@Path("orderId") orderId: Long): Response<ReviewResponse>

    @PATCH("reviews/{reviewId}/reply")
    suspend fun replyToReview(
        @Path("reviewId") reviewId: Long,
        @Query("reply") reply: String
    ): Response<ReviewResponse>
}