package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.characters.CreateCharactersDto
import com.fls.dndproject_frontend.data.source.remote.CharactersServiceClient
import com.fls.dndproject_frontend.domain.model.Characters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CharactersRestRepository(private val charactersServiceClient: CharactersServiceClient) {

    fun getAllCharacters(): Flow<List<Characters>> =
        observeQuery(retryTime = 2000) {
            charactersServiceClient
                .getAllCharacters()
                .map { it.toCharacters() } // Assuming a .toCharacters() extension function on the DTO
        }

    fun getCharactersByUserId(userId: Int): Flow<List<Characters>> = flow {
        var lastResult: List<Characters> = emptyList()
        var isFirstEmission = true

        while (true) {
            try {
                val newResult = charactersServiceClient
                    .getAllCharacters()
                    .map { it.toCharacters() }
                    .filter { it.user?.userId == userId }

                if (isFirstEmission || newResult != lastResult) {
                    lastResult = newResult
                    emit(newResult)
                    isFirstEmission = false
                }
            } catch (e: Exception) {}
            delay(2000)
        }
    }.flowOn(Dispatchers.IO)

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

    suspend fun save(characters: Characters) {
        charactersServiceClient.createCharacter(CreateCharactersDto.fromCharacters(characters))
    }

    suspend fun delete(characterId: Int) {
        charactersServiceClient.deleteCharacter(characterId)
    }
}
