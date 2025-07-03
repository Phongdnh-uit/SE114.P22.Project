package com.example.mam.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal

interface StastiticRepository {
    @GET("stats/get-sold-by-category")
    suspend fun getSoldByCategory(
        @Query("month") month: Int,
        @Query("year") year: Int,
    ): Response<Map<String, BigDecimal>>

    @GET("stats/revenue/month-or-quarter")
    suspend fun getRevenueByMonthOrQuarter(
        @Query("year") year: Int,
        @Query("groupBy") groupBy: String,
    ): Response<Map<Int, BigDecimal>>

    @GET("stats/total-order-count-by-status/active")
    suspend fun  getStatusCount(): Response<Map<String, Long>>
}