package com.fls.dndproject_frontend.data.model.abilities

import com.fls.dndproject_frontend.domain.model.Abilities
import com.fls.dndproject_frontend.data.model.characterClass.CharacterClassDto
import com.fls.dndproject_frontend.data.model.race.RaceDto

data class AbilitiesDto(
    val abilityId: Int,
    val raceAbilities: RaceDto?,
    val classAbilities: CharacterClassDto?
) {
    fun toAbilities() =
        Abilities(
            abilityId = abilityId,
            raceAbilities = raceAbilities?.toRace(),
            classAbilities = classAbilities?.toCharacterClass()
        )
}