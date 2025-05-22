package com.fls.dndproject_frontend.data.model.statsChange

import com.fls.dndproject_frontend.domain.model.StatsChange

data class StatsChangeDto(
    val statsChangeId: Int,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val hitPoints: Int
) {
    fun toStatsChange() =
        StatsChange(
            statsChangeId = statsChangeId,
            strength = strength,
            dexterity = dexterity,
            constitution = constitution,
            intelligence = intelligence,
            wisdom = wisdom,
            charisma = charisma,
            hitPoints = hitPoints
        )
}