package com.fls.dndproject_frontend.domain.model

data class Background(
    val backgroundId: Int = 0,
    val name: String,
    val equipmentText: String?,
    val competenciesText: String?
)