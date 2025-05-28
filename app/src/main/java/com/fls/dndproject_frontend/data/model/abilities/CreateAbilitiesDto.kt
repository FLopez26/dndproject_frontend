package com.fls.dndproject_frontend.data.model.abilities

import com.fls.dndproject_frontend.domain.model.Abilities

data class CreateAbilitiesDto(
    val raceId: Int?,
    val characterClassId: Int?
) {
    companion object {
        fun fromAbilities(abilities: Abilities) =
            CreateAbilitiesDto(
                raceId = abilities.race?.raceId,
                characterClassId = abilities.characterClass?.classId
            )
    }
}