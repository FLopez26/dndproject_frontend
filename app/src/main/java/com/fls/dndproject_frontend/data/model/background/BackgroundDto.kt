package com.fls.dndproject_frontend.data.model.background

import com.fls.dndproject_frontend.domain.model.Background

data class BackgroundDto(
    val backgroundId: Int,
    val name: String,
    val equipmentText: String?,
    val competenciesText: String?
) {
    fun toBackground() =
        Background(
            backgroundId = backgroundId,
            name = name,
            equipmentText = equipmentText,
            competenciesText = competenciesText
        )
}