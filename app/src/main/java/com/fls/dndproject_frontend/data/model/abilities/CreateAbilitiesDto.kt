package com.fls.dndproject_frontend.data.model.abilities

import com.fls.dndproject_frontend.domain.model.Abilities

data class CreateAbilitiesDto(
    val raceAbilitiesId: Int?,
    val classAbilitiesId: Int?
) {
    companion object {
        fun fromAbilities(abilities: Abilities) =
            CreateAbilitiesDto(
                raceAbilitiesId = abilities.raceAbilities?.raceId,
                classAbilitiesId = abilities.classAbilities?.classId
            )
    }
}