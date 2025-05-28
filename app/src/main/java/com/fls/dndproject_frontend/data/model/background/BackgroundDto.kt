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
}