package com.fls.dndproject_frontend.data.model.background

import com.fls.dndproject_frontend.domain.model.Background

data class CreateBackgroundDto(
    val name: String,
    val equipmentText: String?,
    val competenciesText: String?
) {
    companion object {
        fun fromBackground(background: Background) =
            CreateBackgroundDto(
                name = background.name,
                equipmentText = background.equipmentText,
                competenciesText = background.competenciesText
            )
    }

    fun toBackground(id: Int) =
        Background(
            backgroundId = id,
            name = name,
            equipmentText = equipmentText,
            competenciesText = competenciesText
        )
}