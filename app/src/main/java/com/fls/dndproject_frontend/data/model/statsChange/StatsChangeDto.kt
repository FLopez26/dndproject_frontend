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
    companion object {
        fun fromStatsChange(statsChange: StatsChange): StatsChangeDto {
            return StatsChangeDto(
                statsChangeId = statsChange.statsChangeId ?: 0,
                strength = statsChange.strength,
                dexterity = statsChange.dexterity,
                constitution = statsChange.constitution,
                intelligence = statsChange.intelligence,
                wisdom = statsChange.wisdom,
                charisma = statsChange.charisma,
                hitPoints = statsChange.hitPoints
            )
        }
    }
}