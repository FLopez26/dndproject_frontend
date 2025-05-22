package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.statsChange.CreateStatsChangeDto
import com.fls.dndproject_frontend.data.model.statsChange.StatsChangeDto
import retrofit2.http.*

interface StatsChangeServiceClient {

    @GET("api/stats-changes")
    suspend fun getAllStatsChanges(): List<StatsChangeDto>

    @GET("api/stats-changes/{id}")
    suspend fun getStatsChangeById(@Path("id") id: Int): StatsChangeDto?

    @POST("api/stats-changes")
    suspend fun createStatsChange(@Body statsChange: CreateStatsChangeDto): StatsChangeDto

    @PUT("api/stats-changes/{id}")
    suspend fun updateStatsChange(@Path("id") id: Int, @Body statsChange: StatsChangeDto): StatsChangeDto?

    @DELETE("api/stats-changes/{id}")
    suspend fun deleteStatsChange(@Path("id") id: Int)
}