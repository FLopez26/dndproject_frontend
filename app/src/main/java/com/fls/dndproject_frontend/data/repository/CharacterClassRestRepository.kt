package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.characterClass.CreateCharacterClassDto
import com.fls.dndproject_frontend.data.source.remote.CharacterClassServiceClient
import com.fls.dndproject_frontend.domain.model.CharacterClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CharacterClassRestRepository(private val characterClassServiceClient: CharacterClassServiceClient) {

    fun getAllCharacterClasses(): Flow<List<CharacterClass>> =
        observeQuery(retryTime = 2000) {
            characterClassServiceClient
                .getAllCharacterClasses()
                .map { it.toCharacterClass() }
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

    suspend fun save(characterClass: CharacterClass) {
        characterClassServiceClient.createCharacterClass(CreateCharacterClassDto.fromCharacterClass(characterClass))
    }

    suspend fun delete(characterClassId: Int) {
        characterClassServiceClient.deleteCharacterClass(characterClassId)
    }
}
