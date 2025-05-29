package com.fls.dndproject_frontend.domain.usecase.characters

import com.fls.dndproject_frontend.data.repository.CharactersRestRepository
import com.fls.dndproject_frontend.domain.model.Characters
import kotlinx.coroutines.flow.Flow

class GetAllCharactersUseCase(
    private val charactersRestRepository: CharactersRestRepository
) {
    operator fun invoke(): Flow<List<Characters>> {
        return charactersRestRepository.getAllCharacters()
    }
}