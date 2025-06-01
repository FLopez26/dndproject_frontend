package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.stats.CreateStatsDto
import com.fls.dndproject_frontend.data.source.remote.StatsServiceClient
import com.fls.dndproject_frontend.domain.model.Stats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StatsRestRepository(private val statsServiceClient: StatsServiceClient) {

    fun getAllStats(): Flow<List<Stats>> =
        observeQuery(retryTime = 2000) {
            statsServiceClient
                .getAllStats()
                .map { it.toStats() }
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

    suspend fun save(stats: Stats) {
        statsServiceClient.createStats(CreateStatsDto.fromStats(stats))
    }

    suspend fun delete(statsId: Int) {
        statsServiceClient.deleteStats(statsId)
    }
}
