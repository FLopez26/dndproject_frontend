package com.fls.dndproject_frontend.data.model.abilities

import com.fls.dndproject_frontend.domain.model.Abilities
import com.fls.dndproject_frontend.data.model.characterClass.CharacterClassDto
import com.fls.dndproject_frontend.data.model.race.RaceDto

data class AbilitiesDto(
    val abilityId: Int,
    val race: RaceDto?,
    val characterClass: CharacterClassDto?
) {
    fun toAbilities() =
        Abilities(
            abilityId = abilityId,
            race = race?.toRace(),
            characterClass = characterClass?.toCharacterClass()
        )
}