package com.fls.dndproject_frontend.data.model.competencies

import com.fls.dndproject_frontend.domain.model.Competencies
import com.fls.dndproject_frontend.data.model.characterClass.CharacterClassDto
import com.fls.dndproject_frontend.data.model.background.BackgroundDto

data class CompetenciesDto(
    val competencyId: Int,
    val characterClass: CharacterClassDto?,
    val background: BackgroundDto?
) {
    fun toCompetencies() =
        Competencies(
            competencyId = competencyId,
            characterClass = characterClass?.toCharacterClass(),
            background = background?.toBackground()
        )
}