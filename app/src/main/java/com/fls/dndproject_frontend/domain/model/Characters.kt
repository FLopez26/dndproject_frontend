package com.fls.dndproject_frontend.domain.model

data class Characters(
    val characterId: Int?,

    val name: String,
    val description: String?,

    val personalityTraits: String?,
    val ideals: String?,
    val bonds: String?,
    val flaws: String?,

    val stats: Stats?,
    val characterRace: Race?,
    val characterClass: CharacterClass?,
    val background: Background?,

    val abilities: Abilities?,
    val equipment: Equipment?,
    val competencies: Competencies?,

    val image: String?,
    val isPublic: Boolean?,

    val user: User?
)