package com.fls.dndproject_frontend.data.model.competencies

import com.fls.dndproject_frontend.domain.model.Competencies

data class CreateCompetenciesDto(
    val characterClassId: Int?,
    val backgroundId: Int?
) {
    companion object {
        fun fromCompetencies(competencies: Competencies) =
            CreateCompetenciesDto(
                characterClassId = competencies.characterClass?.classId,
                backgroundId = competencies.background?.backgroundId
            )
    }
}