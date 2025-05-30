package com.fls.dndproject_frontend.data.model.race

import com.fls.dndproject_frontend.domain.model.Race
import com.fls.dndproject_frontend.data.model.statsChange.StatsChangeDto

data class RaceDto(
    val raceId: Int,
    val name: String,
    val raceSelection: Int?,
    val speed: Int?,
    val abilities: String?,
    val competencies: String?,
    val statsChange: StatsChangeDto?
) {
    fun toRace() =
        Race(
            raceId = raceId,
            name = name,
            raceSelection = raceSelection,
            speed = speed,
            abilities = abilities,
            competencies = competencies,
            statsChange = statsChange?.toStatsChange()
        )

    companion object {
        fun fromRace(race: Race): RaceDto {
            return RaceDto(
                raceId = race.raceId ?: 0,
                name = race.name,
                raceSelection = race.raceSelection,
                speed = race.speed,
                abilities = race.abilities,
                competencies = race.competencies,
                statsChange = race.statsChange?.let { StatsChangeDto.fromStatsChange(it) }
            )
        }
    }
}