package com.fls.dndproject_frontend.domain.model

data class Competencies(
    val competencyId: Int?,
    val characterClass: CharacterClass? = null,
    val background: Background? = null
)