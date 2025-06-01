package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.statsChange.CreateStatsChangeDto
import com.fls.dndproject_frontend.data.source.remote.StatsChangeServiceClient
import com.fls.dndproject_frontend.domain.model.StatsChange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StatsChangeRestRepository(private val statsChangeServiceClient: StatsChangeServiceClient) {

    fun getAllStatsChanges(): Flow<List<StatsChange>> =
        observeQuery(retryTime = 2000) {
            statsChangeServiceClient
                .getAllStatsChanges()
                .map { it.toStatsChange() }
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
            }
            delay(retryTime)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun save(statsChange: StatsChange) {
        statsChangeServiceClient.createStatsChange(CreateStatsChangeDto.fromStatsChange(statsChange))
    }

    suspend fun delete(statsChangeId: Int) {
        statsChangeServiceClient.deleteStatsChange(statsChangeId)
    }
}
