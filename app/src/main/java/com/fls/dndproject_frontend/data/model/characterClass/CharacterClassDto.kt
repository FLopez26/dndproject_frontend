package com.fls.dndproject_frontend.data.model.characterClass

import com.fls.dndproject_frontend.domain.model.CharacterClass

data class CharacterClassDto(
    val classId: Int,
    val name: String,
    val selection: Int?,
    val diceHitPoints: String?,
    val equipment: String?,
    val competencies: String?,
    val abilities: String?
) {
    fun toCharacterClass() =
        CharacterClass(
            classId = classId,
            name = name,
            selection = selection,
            diceHitPoints = diceHitPoints,
            equipment = equipment,
            competencies = competencies,
            abilities = abilities
        )
    companion object {
        fun fromCharacterClass(characterClass: CharacterClass): CharacterClassDto {
            return CharacterClassDto(
                classId = characterClass.classId,
                name = characterClass.name,
                selection = characterClass.selection,
                diceHitPoints = characterClass.diceHitPoints,
                equipment = characterClass.equipment,
                competencies = characterClass.competencies,
                abilities = characterClass.abilities
            )
        }
    }
}