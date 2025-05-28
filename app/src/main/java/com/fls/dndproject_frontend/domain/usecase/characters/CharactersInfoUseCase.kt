package com.fls.dndproject_frontend.domain.usecase.characters

import com.fls.dndproject_frontend.data.repository.CharactersRestRepository
import com.fls.dndproject_frontend.domain.model.Characters

class CharactersInfoUseCase(
    private val charactersRepository: CharactersRestRepository
) {
    suspend operator fun invoke(characterId: Int): Result<Characters> {
        return charactersRepository.getCharacterById(characterId)
    }
}