package com.fls.dndproject_frontend.domain.model

data class Equipment(
    val equipmentId: Int?,
    val characterClass: CharacterClass? = null,
    val background: Background? = null
)