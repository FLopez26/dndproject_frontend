package com.fls.dndproject_frontend.data.model.competencies

import com.fls.dndproject_frontend.domain.model.Competencies
import com.fls.dndproject_frontend.data.model.characterClass.CharacterClassDto
import com.fls.dndproject_frontend.data.model.background.BackgroundDto

data class CompetenciesDto(
    val competencyId: Int,
    val classCompetencies: CharacterClassDto?,
    val backgroundCompetencies: BackgroundDto?
) {
    fun toCompetencies() =
        Competencies(
            competencyId = competencyId,
            classCompetencies = classCompetencies?.toCharacterClass(),
            backgroundCompetencies = backgroundCompetencies?.toBackground()
        )
}