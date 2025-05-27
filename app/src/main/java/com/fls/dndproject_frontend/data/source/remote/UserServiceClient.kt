package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.model.user.CreateUserDto
import com.fls.dndproject_frontend.data.model.user.UserDto
import retrofit2.http.*

interface UserServiceClient {

    @GET("api/users")
    suspend fun getAllUsers(): List<UserDto>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto?

    @POST("api/users")
    suspend fun createUser(@Body user: CreateUserDto): UserDto

    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserDto): UserDto?

    @DELETE("api/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int)
}