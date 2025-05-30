package com.fls.dndproject_frontend.domain.model

data class Abilities(
    val abilityId: Int?,
    val race: Race? = null,
    val characterClass: CharacterClass? = null
)