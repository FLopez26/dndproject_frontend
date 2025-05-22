package com.fls.dndproject_frontend.data.model.equipment

import com.fls.dndproject_frontend.domain.model.Equipment

data class CreateEquipmentDto(
    val classEquipmentId: Int?,
    val backgroundEquipmentId: Int?
) {
    companion object {
        fun fromEquipment(equipment: Equipment) =
            CreateEquipmentDto(
                classEquipmentId = equipment.classEquipment?.classId,
                backgroundEquipmentId = equipment.backgroundEquipment?.backgroundId
            )
    }
}