package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.competencies.CompetenciesDto
import com.fls.dndproject_frontend.data.model.competencies.CreateCompetenciesDto
import retrofit2.http.*

interface CompetenciesServiceClient {

    @GET("api/competencies")
    suspend fun getAllCompetencies(): List<CompetenciesDto>

    @GET("api/competencies/{id}")
    suspend fun getCompetenciesById(@Path("id") id: Int): CompetenciesDto?

    @POST("api/competencies")
    suspend fun createCompetencies(@Body competencies: CreateCompetenciesDto): CompetenciesDto

    @PUT("api/competencies/{id}")
    suspend fun updateCompetencies(@Path("id") id: Int, @Body competencies: CompetenciesDto): CompetenciesDto?

    @DELETE("api/competencies/{id}")
    suspend fun deleteCompetencies(@Path("id") id: Int)
}