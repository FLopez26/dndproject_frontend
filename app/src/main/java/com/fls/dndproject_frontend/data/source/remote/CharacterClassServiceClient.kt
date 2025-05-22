package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.characterClass.CharacterClassDto
import com.fls.dndproject_frontend.data.model.characterClass.CreateCharacterClassDto
import retrofit2.http.*

interface CharacterClassServiceClient {

    @GET("api/classes")
    suspend fun getAllCharacterClasses(): List<CharacterClassDto>

    @GET("api/classes/{id}")
    suspend fun getCharacterClassById(@Path("id") id: Int): CharacterClassDto?

    @POST("api/classes")
    suspend fun createCharacterClass(@Body characterClass: CreateCharacterClassDto): CharacterClassDto

    @PUT("api/classes/{id}")
    suspend fun updateCharacterClass(@Path("id") id: Int, @Body characterClass: CharacterClassDto): CharacterClassDto?

    @DELETE("api/classes/{id}")
    suspend fun deleteCharacterClass(@Path("id") id: Int)
}