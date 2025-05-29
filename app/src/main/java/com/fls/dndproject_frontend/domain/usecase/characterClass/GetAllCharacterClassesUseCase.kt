package com.fls.dndproject_frontend.domain.usecase.characterClass

import com.fls.dndproject_frontend.data.repository.CharacterClassRestRepository
import com.fls.dndproject_frontend.domain.model.CharacterClass
import kotlinx.coroutines.flow.Flow

class GetAllCharacterClassesUseCase(
    private val characterClassRepository: CharacterClassRestRepository
) {
    operator fun invoke(): Flow<List<CharacterClass>> {
        return characterClassRepository.getAllCharacterClasses()
    }
}