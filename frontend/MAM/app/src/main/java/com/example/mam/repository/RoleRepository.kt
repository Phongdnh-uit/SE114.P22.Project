package com.example.mam.repository

import com.example.mam.dto.role.RoleRequest
import com.example.mam.dto.role.RoleResponse
import com.example.mam.dto.vo.PageVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RoleRepository {
    @GET("roles/{id}")
    suspend fun getRoleById(@Path("id") id: Long): Response<RoleResponse>
    @PUT("roles/{id}")
    suspend fun updateRole(
        @Path("id") id: Long,
        @Body roleRequest: RoleRequest
    ): Response<RoleResponse>
    @DELETE("roles/{id}")
    suspend fun deleteRole(@Path("id") id: Long): Response<Unit>
    @GET("roles")
    suspend fun getAllRoles(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: List<String>? = null,
        @Query("filter") filter: String
    ): Response<PageVO<RoleResponse>>
    @POST("roles")
    suspend fun createRole(@Body roleRequest: RoleRequest): Response<RoleResponse>
}