package com.fls.dndproject_frontend.data.model.background

import com.fls.dndproject_frontend.domain.model.Background

data class BackgroundDto(
    val backgroundId: Int,
    val name: String,
    val equipment: String?,
    val competencies: String?
) {
    fun toBackground() =
        Background(
            backgroundId = backgroundId,
            name = name,
            equipment = equipment,
            competencies = competencies
        )
    companion object {
        fun fromBackground(background: Background): BackgroundDto {
            return BackgroundDto(
                backgroundId = background.backgroundId ?: 0,
                name = background.name,
                equipment = background.equipment,
                competencies = background.competencies
            )
        }
    }
}