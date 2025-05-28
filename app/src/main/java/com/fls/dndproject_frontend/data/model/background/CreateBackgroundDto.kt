package com.fls.dndproject_frontend.data.model.background

import com.fls.dndproject_frontend.domain.model.Background

data class CreateBackgroundDto(
    val name: String,
    val equipment: String?,
    val competencies: String?
) {
    companion object {
        fun fromBackground(background: Background) =
            CreateBackgroundDto(
                name = background.name,
                equipment = background.equipment,
                competencies = background.competencies
            )
    }

    fun toBackground(id: Int) =
        Background(
            backgroundId = id,
            name = name,
            equipment = equipment,
            competencies = competencies
        )
}