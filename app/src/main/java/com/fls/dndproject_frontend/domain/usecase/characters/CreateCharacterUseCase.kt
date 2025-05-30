package com.fls.dndproject_frontend.domain.usecase.characters

import com.fls.dndproject_frontend.data.repository.CharactersRestRepository
import com.fls.dndproject_frontend.domain.model.Characters

class CreateCharacterUseCase(
    private val charactersRepository: CharactersRestRepository
) {
    // El tipo de retorno debe ser Result<Unit>
    suspend operator fun invoke(characterToSave: Characters): Result<Unit> {
        return try {
            // Llama a la función 'save' del repositorio
            charactersRepository.save(characterToSave)
            // Si no hay excepción, significa que la operación fue exitosa
            Result.success(Unit)
        } catch (e: Exception) {
            // Si ocurre una excepción, la captura y retorna un Result.failure
            Result.failure(e)
        }
    }
}