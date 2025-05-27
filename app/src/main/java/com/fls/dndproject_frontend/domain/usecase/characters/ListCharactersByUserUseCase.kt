// domain/usecase/characters/ListCharactersByUserUseCase.kt
package com.fls.dndproject_frontend.domain.usecase.characters

import com.fls.dndproject_frontend.data.repository.CharactersRestRepository
import com.fls.dndproject_frontend.domain.model.Characters
import kotlinx.coroutines.flow.Flow

class ListCharactersByUserUseCase(
    private val charactersRepository: CharactersRestRepository
) {
    operator fun invoke(userId: Int): Flow<List<Characters>> {
        return charactersRepository.getCharactersByUserId(userId)
    }
}