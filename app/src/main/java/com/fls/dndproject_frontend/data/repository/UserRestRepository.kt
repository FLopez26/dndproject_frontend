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
     * Esta función sirve para hacer consultas a un servicio de manera continua,
     * emitiendo el resultado cuando cambia o en la primera vez.
     */
    fun <T> observeQuery(retryTime: Long = 5000, query: suspend () -> List<T>): Flow<List<T>> = flow {
        var lastResult: List<T> = emptyList()
        var isFirstEmission = true

        while (true) {
            try {
                val newResult = query()

                if (isFirstEmission || newResult != lastResult) {
                    lastResult = newResult
                    emit(newResult)
                    isFirstEmission = false
                }
            } catch (e: Exception) {}
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