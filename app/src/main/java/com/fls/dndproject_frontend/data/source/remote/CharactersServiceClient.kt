package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.characters.CharactersDto
import com.fls.dndproject_frontend.data.model.characters.CreateCharactersDto
import retrofit2.http.*

interface CharactersServiceClient {

    @GET("api/characters")
    suspend fun getAllCharacters(): List<CharactersDto>

    @GET("api/characters/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharactersDto?

    @POST("api/characters")
    suspend fun createCharacter(@Body character: CreateCharactersDto): CharactersDto

    @PUT("api/characters/{id}")
    suspend fun updateCharacter(@Path("id") id: Int, @Body character: CharactersDto): CharactersDto?

    @DELETE("api/characters/{id}")
    suspend fun deleteCharacter(@Path("id") id: Int)
}