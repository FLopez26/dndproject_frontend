package com.fls.dndproject_frontend.data.model.equipment

import com.fls.dndproject_frontend.domain.model.Equipment
import com.fls.dndproject_frontend.data.model.characterClass.CharacterClassDto
import com.fls.dndproject_frontend.data.model.background.BackgroundDto

data class EquipmentDto(
    val equipmentId: Int,
    val characterClass: CharacterClassDto?,
    val background: BackgroundDto?
) {
    fun toEquipment() =
        Equipment(
            equipmentId = equipmentId,
            characterClass = characterClass?.toCharacterClass(),
            background = background?.toBackground()
        )
}