package com.fls.dndproject_frontend.data.model.stats

import com.fls.dndproject_frontend.data.model.statsChange.CreateStatsChangeDto
import com.fls.dndproject_frontend.domain.model.Stats

data class CreateStatsDto(
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val hitPoints: Int,
    val statsChange: CreateStatsChangeDto?
) {
    companion object {
        fun fromStats(stats: Stats) =
            CreateStatsDto(
                strength = stats.strength,
                dexterity = stats.dexterity,
                constitution = stats.constitution,
                intelligence = stats.intelligence,
                wisdom = stats.wisdom,
                charisma = stats.charisma,
                hitPoints = stats.hitPoints,
                statsChange = stats.statsChange?.let { CreateStatsChangeDto.fromStatsChange(it) }
            )
    }

    fun toStats(id: Int) =
        Stats(
            statsId = id,
            strength = strength,
            dexterity = dexterity,
            constitution = constitution,
            intelligence = intelligence,
            wisdom = wisdom,
            charisma = charisma,
            hitPoints = hitPoints,
            statsChange = statsChange?.toStatsChange(0)
        )
}