package com.fls.dndproject_frontend.data.model.equipment

import com.fls.dndproject_frontend.domain.model.Equipment

data class CreateEquipmentDto(
    val characterClassId: Int?,
    val backgroundId: Int?
) {
    companion object {
        fun fromEquipment(equipment: Equipment) =
            CreateEquipmentDto(
                characterClassId = equipment.characterClass?.classId,
                backgroundId = equipment.background?.backgroundId
            )
    }
}