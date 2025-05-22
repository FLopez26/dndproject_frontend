package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.abilities.AbilitiesDto
import com.fls.dndproject_frontend.data.model.abilities.CreateAbilitiesDto
import retrofit2.http.*

interface AbilitiesServiceClient {

    @GET("api/abilities")
    suspend fun getAllAbilities(): List<AbilitiesDto>

    @GET("api/abilities/{id}")
    suspend fun getAbilityById(@Path("id") id: Int): AbilitiesDto?

    @POST("api/abilities")
    suspend fun createAbility(@Body abilities: CreateAbilitiesDto): AbilitiesDto

    @PUT("api/abilities/{id}")
    suspend fun updateAbility(@Path("id") id: Int, @Body abilities: AbilitiesDto): AbilitiesDto?

    @DELETE("api/abilities/{id}")
    suspend fun deleteAbility(@Path("id") id: Int)
}