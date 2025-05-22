package com.fls.dndproject_frontend.data.model.equipment

import com.fls.dndproject_frontend.domain.model.Equipment
import com.fls.dndproject_frontend.data.model.characterClass.CharacterClassDto
import com.fls.dndproject_frontend.data.model.background.BackgroundDto

data class EquipmentDto(
    val equipmentId: Int,
    val classEquipment: CharacterClassDto?,
    val backgroundEquipment: BackgroundDto?
) {
    fun toEquipment() =
        Equipment(
            equipmentId = equipmentId,
            classEquipment = classEquipment?.toCharacterClass(),
            backgroundEquipment = backgroundEquipment?.toBackground()
        )
}