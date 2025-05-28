package com.fls.dndproject_frontend.data.model.characterClass

import com.fls.dndproject_frontend.domain.model.CharacterClass

data class CreateCharacterClassDto(
    val name: String,
    val selection: Int?,
    val diceHitPoints: String?,
    val equipment: String?,
    val competencies: String?,
    val abilities: String?
) {
    companion object {
        fun fromCharacterClass(characterClass: CharacterClass) =
            CreateCharacterClassDto(
                name = characterClass.name,
                selection = characterClass.selection,
                diceHitPoints = characterClass.diceHitPoints,
                equipment = characterClass.equipment,
                competencies = characterClass.competencies,
                abilities = characterClass.abilities
            )
    }

    fun toCharacterClass(id: Int) =
        CharacterClass(
            classId = id,
            name = name,
            selection = selection,
            diceHitPoints = diceHitPoints,
            equipment = equipment,
            competencies = competencies,
            abilities = abilities
        )
}