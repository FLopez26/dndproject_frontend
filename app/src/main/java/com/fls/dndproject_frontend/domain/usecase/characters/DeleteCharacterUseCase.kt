package com.fls.dndproject_frontend.domain.usecase.characters

import com.fls.dndproject_frontend.data.repository.CharactersRestRepository

class DeleteCharacterUseCase(
    val charactersRepository: CharactersRestRepository
) {
    suspend operator fun invoke(characterId: Int){
        charactersRepository.delete(characterId)
    }
}