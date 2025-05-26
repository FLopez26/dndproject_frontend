package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.competencies.CreateCompetenciesDto
import com.fls.dndproject_frontend.data.source.remote.CompetenciesServiceClient
import com.fls.dndproject_frontend.domain.model.Competencies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CompetenciesRestRepository(private val competenciesServiceClient: CompetenciesServiceClient) {

    fun getAllCompetencies(): Flow<List<Competencies>> =
        observeQuery(retryTime = 2000) {
            competenciesServiceClient
                .getAllCompetencies()
                .map { it.toCompetencies() } // Assuming a .toCompetencies() extension function on the DTO
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
                // Log the exception here if needed for debugging, but suppress for continuous operation
            }
            delay(retryTime)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun save(competencies: Competencies) {
        competenciesServiceClient.createCompetencies(CreateCompetenciesDto.fromCompetencies(competencies))
    }

    suspend fun delete(competenciesId: Int) {
        competenciesServiceClient.deleteCompetencies(competenciesId)
    }
}
