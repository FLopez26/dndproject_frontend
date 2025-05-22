package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.background.BackgroundDto
import com.fls.dndproject_frontend.data.model.background.CreateBackgroundDto
import retrofit2.http.*

interface BackgroundServiceClient {

    @GET("api/backgrounds")
    suspend fun getAllBackgrounds(): List<BackgroundDto>

    @GET("api/backgrounds/{id}")
    suspend fun getBackgroundById(@Path("id") id: Int): BackgroundDto?

    @POST("api/backgrounds")
    suspend fun createBackground(@Body background: CreateBackgroundDto): BackgroundDto

    @PUT("api/backgrounds/{id}")
    suspend fun updateBackground(@Path("id") id: Int, @Body background: BackgroundDto): BackgroundDto?

    @DELETE("api/backgrounds/{id}")
    suspend fun deleteBackground(@Path("id") id: Int)
}