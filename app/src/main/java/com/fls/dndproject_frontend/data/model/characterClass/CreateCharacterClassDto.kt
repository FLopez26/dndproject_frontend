package com.fls.dndproject_frontend.data.model.characterClass

import com.fls.dndproject_frontend.domain.model.CharacterClass

data class CreateCharacterClassDto(
    val name: String,
    val selection: Int?,
    val hitPointsDice: String?,
    val equipmentText: String?,
    val competenciesText: String?,
    val abilitiesText: String?
) {
    companion object {
        fun fromCharacterClass(characterClass: CharacterClass) =
            CreateCharacterClassDto(
                name = characterClass.name,
                selection = characterClass.selection,
                hitPointsDice = characterClass.hitPointsDice,
                equipmentText = characterClass.equipmentText,
                competenciesText = characterClass.competenciesText,
                abilitiesText = characterClass.abilitiesText
            )
    }

    fun toCharacterClass(id: Int) =
        CharacterClass(
            classId = id,
            name = name,
            selection = selection,
            hitPointsDice = hitPointsDice,
            equipmentText = equipmentText,
            competenciesText = competenciesText,
            abilitiesText = abilitiesText
        )
}