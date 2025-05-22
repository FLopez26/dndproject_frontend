package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.abilities.CreateAbilitiesDto
import com.fls.dndproject_frontend.data.source.remote.AbilitiesServiceClient
import com.fls.dndproject_frontend.domain.model.Abilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AbilitiesRestRepository(private val abilitiesServiceClient: AbilitiesServiceClient) {

    fun getAllAbilities(): Flow<List<Abilities>> =
        observeQuery(retryTime = 2000) {
            abilitiesServiceClient
                .getAllAbilities()
                .map { it.toAbilities() }
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

    suspend fun save(abilities: Abilities) {
        abilitiesServiceClient.createAbility(CreateAbilitiesDto.fromAbilities(abilities))
    }

    suspend fun delete(abilitiesId: Int) {
        abilitiesServiceClient.deleteAbility(abilitiesId)
    }
}