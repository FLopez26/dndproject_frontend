package com.fls.dndproject_frontend.domain.model

data class Equipment(
    val equipmentId: Int = 0,
    val classEquipment: CharacterClass? = null,
    val backgroundEquipment: Background? = null
)