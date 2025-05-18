package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.user.CreateUserDto
import com.fls.dndproject_frontend.data.source.remote.UserServiceClient
import com.fls.dndproject_frontend.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRestRepository(val userServiceClient: UserServiceClient) {

    fun getAllUsers(): Flow<List<User>> =
        observeQuery(retryTime = 2000) {
            userServiceClient
                .getAllUsers()
                .map { it.toUser() }
        }

    /**
     * Esta funcion sirve para hacer consultas a un servicio de manera continua
     */
    fun <T> observeQuery(retryTime: Long = 5000, query: suspend () -> List<T>): Flow<List<T>> = flow {
        var lastResult: List<T> = emptyList()
        while (true) {
            try {
                val newResult = query()
                if (newResult != lastResult) {
                    lastResult = newResult
                    emit(newResult)
                }
            } catch (e: Exception) {
                println("Error al obtener datos: ${e.message}")
            }
            delay(retryTime)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun save(user: User) {
        userServiceClient.createUser(CreateUserDto.fromUser(user))
    }

    suspend fun delete(userId: Int) {
        userServiceClient.deleteUser(userId)
    }
}