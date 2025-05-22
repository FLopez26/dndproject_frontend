package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.equipment.EquipmentDto
import com.fls.dndproject_frontend.data.model.equipment.CreateEquipmentDto
import retrofit2.http.*

interface EquipmentServiceClient {

    @GET("api/equipment")
    suspend fun getAllEquipment(): List<EquipmentDto>

    @GET("api/equipment/{id}")
    suspend fun getEquipmentById(@Path("id") id: Int): EquipmentDto?

    @POST("api/equipment")
    suspend fun createEquipment(@Body equipment: CreateEquipmentDto): EquipmentDto

    @PUT("api/equipment/{id}")
    suspend fun updateEquipment(@Path("id") id: Int, @Body equipment: EquipmentDto): EquipmentDto?

    @DELETE("api/equipment/{id}")
    suspend fun deleteEquipment(@Path("id") id: Int)
}