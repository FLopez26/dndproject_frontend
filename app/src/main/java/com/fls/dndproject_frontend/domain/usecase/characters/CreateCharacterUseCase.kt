package com.fls.dndproject_frontend.domain.usecase.characters

import com.fls.dndproject_frontend.data.repository.CharactersRestRepository
import com.fls.dndproject_frontend.domain.model.Characters

class CreateCharacterUseCase(
    private val charactersRepository: CharactersRestRepository
) {
    suspend operator fun invoke(characterToSave: Characters): Result<Unit> {
        return try {
            charactersRepository.save(characterToSave)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}