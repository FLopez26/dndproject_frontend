package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.race.RaceDto
import com.fls.dndproject_frontend.data.model.race.CreateRaceDto
import retrofit2.http.*

interface RaceServiceClient {

    @GET("api/races")
    suspend fun getAllRaces(): List<RaceDto>

    @GET("api/races/{id}")
    suspend fun getRaceById(@Path("id") id: Int): RaceDto?

    @POST("api/races")
    suspend fun createRace(@Body race: CreateRaceDto): RaceDto

    @PUT("api/races/{id}")
    suspend fun updateRace(@Path("id") id: Int, @Body race: RaceDto): RaceDto?

    @DELETE("api/races/{id}")
    suspend fun deleteRace(@Path("id") id: Int)
}