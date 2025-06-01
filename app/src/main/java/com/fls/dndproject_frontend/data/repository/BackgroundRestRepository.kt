package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.background.CreateBackgroundDto
import com.fls.dndproject_frontend.data.source.remote.BackgroundServiceClient
import com.fls.dndproject_frontend.domain.model.Background
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BackgroundRestRepository(private val backgroundServiceClient: BackgroundServiceClient) {

    fun getAllBackgrounds(): Flow<List<Background>> =
        observeQuery(retryTime = 2000) {
            backgroundServiceClient
                .getAllBackgrounds()
                .map { it.toBackground() }
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
            } catch (e: Exception) {}
            delay(retryTime)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun save(background: Background) {
        backgroundServiceClient.createBackground(CreateBackgroundDto.fromBackground(background))
    }

    suspend fun delete(backgroundId: Int) {
        backgroundServiceClient.deleteBackground(backgroundId)
    }
}