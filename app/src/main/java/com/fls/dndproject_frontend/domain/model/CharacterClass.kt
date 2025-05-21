package com.fls.dndproject_frontend.domain.model

data class CharacterClass(
    val id: Int = 0,
    val name: String,
    val selection: Int?,
    val hitPointsDice: String?,
    val equipmentText: String?,
    val competenciesText: String?,
    val abilitiesText: String?
)