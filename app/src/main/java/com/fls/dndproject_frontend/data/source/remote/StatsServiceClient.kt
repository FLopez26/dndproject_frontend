package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.stats.StatsDto
import com.fls.dndproject_frontend.data.model.stats.CreateStatsDto
import retrofit2.http.*

interface StatsServiceClient {

    @GET("api/stats")
    suspend fun getAllStats(): List<StatsDto>

    @GET("api/stats/{id}")
    suspend fun getStatsById(@Path("id") id: Int): StatsDto?

    @POST("api/stats")
    suspend fun createStats(@Body stats: CreateStatsDto): StatsDto

    @PUT("api/stats/{id}")
    suspend fun updateStats(@Path("id") id: Int, @Body stats: StatsDto): StatsDto?

    @DELETE("api/stats/{id}")
    suspend fun deleteStats(@Path("id") id: Int)
}