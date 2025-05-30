package com.fls.dndproject_frontend.domain.model

data class CharacterClass(
    val classId: Int?,
    val name: String,
    val selection: Int?,
    val diceHitPoints: String?,
    val equipment: String?,
    val competencies: String?,
    val abilities: String?
)