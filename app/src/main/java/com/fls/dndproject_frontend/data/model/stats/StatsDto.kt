package com.fls.dndproject_frontend.data.model.stats

import com.fls.dndproject_frontend.domain.model.Stats
import com.fls.dndproject_frontend.data.model.statsChange.StatsChangeDto

data class StatsDto(
    val statsId: Int,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val hitPoints: Int,
    val statsChange: StatsChangeDto?
) {
    fun toStats() =
        Stats(
            statsId = statsId,
            strength = strength,
            dexterity = dexterity,
            constitution = constitution,
            intelligence = intelligence,
            wisdom = wisdom,
            charisma = charisma,
            hitPoints = hitPoints,
            statsChange = statsChange?.toStatsChange()
        )
}