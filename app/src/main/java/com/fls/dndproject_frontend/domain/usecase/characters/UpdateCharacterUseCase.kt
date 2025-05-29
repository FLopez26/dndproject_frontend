package com.fls.dndproject_frontend.domain.usecase.characters

import com.fls.dndproject_frontend.data.repository.CharactersRestRepository
import com.fls.dndproject_frontend.domain.model.Characters

class UpdateCharacterUseCase(
    private val charactersRepository: CharactersRestRepository
) {
    suspend operator fun invoke(characterId: Int, character: Characters): Result<Characters> {
        return charactersRepository.updateCharacter(characterId, character)
    }
}