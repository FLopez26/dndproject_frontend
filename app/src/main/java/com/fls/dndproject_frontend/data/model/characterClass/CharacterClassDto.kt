package com.fls.dndproject_frontend.data.model.characterClass

import com.fls.dndproject_frontend.domain.model.CharacterClass

data class CharacterClassDto(
    val classId: Int,
    val name: String,
    val selection: Int?,
    val hitPointsDice: String?,
    val equipmentText: String?,
    val competenciesText: String?,
    val abilitiesText: String?
) {
    fun toCharacterClass() =
        CharacterClass(
            classId = classId,
            name = name,
            selection = selection,
            hitPointsDice = hitPointsDice,
            equipmentText = equipmentText,
            competenciesText = competenciesText,
            abilitiesText = abilitiesText
        )
}