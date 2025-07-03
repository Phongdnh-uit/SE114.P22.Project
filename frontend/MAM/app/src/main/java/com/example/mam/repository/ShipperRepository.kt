package com.example.mam.repository

import com.example.mam.dto.shipper.ShipperRequest
import com.example.mam.dto.shipper.ShipperResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ShipperRepository {
    @GET("shippers/{id}")
    suspend fun getShipperById(@Path("id") id: Long): Response<ShipperResponse>
    @PUT("shippers/{id}")
    suspend fun updateShipper(
        @Path("id") id: Long,
        @Body shipperRequest: ShipperRequest
    ): Response<ShipperResponse>
    @DELETE("shippers/{id}")
    suspend fun deleteShipper(@Path("id") id: Long): Response<Unit>
    @GET("shippers")
    suspend fun getShippers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String
    ): Response<PageVO<ShipperResponse>>
    @POST("shippers")
    suspend fun createShipper(@Body shipperRequest: ShipperRequest): Response<ShipperResponse>
}