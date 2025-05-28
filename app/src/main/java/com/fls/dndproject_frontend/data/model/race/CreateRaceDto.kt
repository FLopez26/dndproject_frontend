package com.fls.dndproject_frontend.data.model.race

import com.fls.dndproject_frontend.domain.model.Race
import com.fls.dndproject_frontend.data.model.statsChange.CreateStatsChangeDto

data class CreateRaceDto(
    val name: String,
    val raceSelection: Int?,
    val speed: Int?,
    val abilities: String?,
    val competencies: String?,
    val statsChange: CreateStatsChangeDto?
) {
    companion object {
        fun fromRace(race: Race) =
            CreateRaceDto(
                name = race.name,
                raceSelection = race.raceSelection,
                speed = race.speed,
                abilities = race.abilities,
                competencies = race.competencies,
                statsChange = race.statsChange?.let { CreateStatsChangeDto.fromStatsChange(it) }
            )
    }

    fun toRace(id: Int) =
        Race(
            raceId = id,
            name = name,
            raceSelection = raceSelection,
            speed = speed,
            abilities = abilities,
            competencies = competencies,
            statsChange = statsChange?.toStatsChange(0)
        )
}