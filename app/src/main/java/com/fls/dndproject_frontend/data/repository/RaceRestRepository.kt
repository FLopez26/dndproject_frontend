package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.race.CreateRaceDto
import com.fls.dndproject_frontend.data.source.remote.RaceServiceClient
import com.fls.dndproject_frontend.domain.model.Race
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RaceRestRepository(private val raceServiceClient: RaceServiceClient) {

    fun getAllRaces(): Flow<List<Race>> =
        observeQuery(retryTime = 2000) {
            raceServiceClient
                .getAllRaces()
                .map { it.toRace() }
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

    suspend fun save(race: Race) {
        raceServiceClient.createRace(CreateRaceDto.fromRace(race))
    }

    suspend fun delete(raceId: Int) {
        raceServiceClient.deleteRace(raceId)
    }
}
