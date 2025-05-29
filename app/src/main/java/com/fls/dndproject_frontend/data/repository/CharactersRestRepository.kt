package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.characters.CharactersDto
import com.fls.dndproject_frontend.data.model.characters.CreateCharactersDto
import com.fls.dndproject_frontend.data.source.remote.CharactersServiceClient
import com.fls.dndproject_frontend.domain.model.Characters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class CharactersRestRepository(private val charactersServiceClient: CharactersServiceClient) {

    fun getAllCharacters(): Flow<List<Characters>> =
        observeQuery(retryTime = 2000) {
            charactersServiceClient
                .getAllCharacters()
                .map { it.toCharacters() }
        }

    suspend fun getCharacterById(characterId: Int): Result<Characters> =
        withContext(Dispatchers.IO) {
            try {
                val characterDto = charactersServiceClient.getCharacterById(characterId)
                if (characterDto != null) {
                    Result.success(characterDto.toCharacters())
                } else {
                    Result.failure(NoSuchElementException("Personaje con id $characterId no encontrado"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
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

    suspend fun updateCharacter(characterId: Int, character: Characters): Result<Characters> =
        withContext(Dispatchers.IO) {
            try {
                // Mapear el modelo de dominio 'Characters' a un DTO 'CharactersDto'
                // para enviarlo al servicio. Asumo que tienes una función de extensión o un mapper
                // como 'CharactersDto.fromCharacters(character)' que ya existe.
                val characterDtoToUpdate = CharactersDto.fromCharacters(character) // <-- Asegúrate de que este mapper existe

                // Llamar al servicio para realizar la actualización
                val updatedCharacterDto = charactersServiceClient.updateCharacter(characterId, characterDtoToUpdate)

                // Mapear el DTO de respuesta de vuelta al modelo de dominio 'Characters'
                if (updatedCharacterDto != null) {
                    Result.success(updatedCharacterDto.toCharacters()) // <-- Asegúrate de que este mapper existe
                } else {
                    Result.failure(IllegalStateException("La actualización del personaje retornó nulo."))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

}
