package com.fls.dndproject_frontend.data.repository

import android.util.Log
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
            } catch (e: Exception) {
                Log.e("ObserveQuery", "Error durante la consulta: ${e.message}", e)
            }
            delay(retryTime)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun save(user: User): User {
        val createUserDto = CreateUserDto.fromUser(user)
        val createdUserDto = userServiceClient.createUser(createUserDto)
        return createdUserDto.toUser()
    }

    suspend fun delete(userId: Int) {
        userServiceClient.deleteUser(userId)
    }
}