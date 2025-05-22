package com.fls.dndproject_frontend.domain.model

data class Abilities(
    val abilityId: Int = 0,
    val raceAbilities: Race? = null,
    val classAbilities: CharacterClass? = null
)