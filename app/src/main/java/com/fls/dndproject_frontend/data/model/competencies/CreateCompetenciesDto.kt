package com.fls.dndproject_frontend.data.model.competencies

import com.fls.dndproject_frontend.domain.model.Competencies

data class CreateCompetenciesDto(
    val classCompetenciesId: Int?,
    val backgroundCompetenciesId: Int?
) {
    companion object {
        fun fromCompetencies(competencies: Competencies) =
            CreateCompetenciesDto(
                classCompetenciesId = competencies.classCompetencies?.classId,
                backgroundCompetenciesId = competencies.backgroundCompetencies?.backgroundId
            )
    }
}